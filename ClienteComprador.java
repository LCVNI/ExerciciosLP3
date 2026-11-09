import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.*;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class ClienteComprador extends UnicastRemoteObject implements CallbackComprador {
    
    private String compradorId;
    private String nomeComprador;
    private ServicoLeilao servico;
    private Scanner scanner;
    
    // Cores
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String PURPLE = "\u001B[35m";
    private static final String CYAN = "\u001B[36m";
    
    public ClienteComprador() throws RemoteException {
        super();
        this.scanner = new Scanner(System.in);
        // Gerar ID único automaticamente
        this.compradorId = "C" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
    
    // =========================================================================
    // CALLBACKS (NOTIFICAÇÕES DO SERVIDOR)
    // =========================================================================
    
    @Override
    public void aoSerSuperado(String leilaoId, double novoLance, String novoLider) 
            throws RemoteException {
        System.out.println("\n" + YELLOW + "╔════════════════════════════════════════════════════════╗" + RESET);
        System.out.println(YELLOW + "║            VOCÊ FOI SUPERADO!                       ║" + RESET);
        System.out.println(YELLOW + "╚════════════════════════════════════════════════════════╝" + RESET);
        System.out.println("Leilão: " + CYAN + leilaoId + RESET);
        System.out.println("Novo lance: " + RED + "R$ " + String.format("%.2f", novoLance) + RESET);
        System.out.println("Novo líder: " + PURPLE + novoLider + RESET);
        System.out.println(CYAN + "Dica: Faça um lance maior para retomar a liderança!" + RESET);
        System.out.println();
    }
    
    @Override
    public void aoVencerLeilao(String leilaoId, double valorFinal) throws RemoteException {
        System.out.println("\n" + GREEN + "╔════════════════════════════════════════════════════════╗" + RESET);
        System.out.println(GREEN + "║          PARABÉNS! VOCÊ VENCEU O LEILÃO!          ║" + RESET);
        System.out.println(GREEN + "╚════════════════════════════════════════════════════════╝" + RESET);
        System.out.println("Leilão: " + CYAN + leilaoId + RESET);
        System.out.println("Valor final: " + GREEN + "R$ " + String.format("%.2f", valorFinal) + RESET);
        System.out.println(YELLOW + "📞 Aguarde contato do vendedor para finalizar a compra." + RESET);
        System.out.println();
    }
    
    public void inicializar() {
        limparTela();
        System.out.println(CYAN + "╔════════════════════════════════════════════════════════╗" + RESET);
        System.out.println(CYAN + "║     BEM-VINDO AO SISTEMA DE LEILÃO - COMPRADOR        ║" + RESET);
        System.out.println(CYAN + "╚════════════════════════════════════════════════════════╝" + RESET);
        System.out.println();
        
        // Solicitar nome do comprador
        System.out.print(YELLOW + "Digite seu nome: " + RESET);
        this.nomeComprador = scanner.nextLine().trim();
        
        while (this.nomeComprador.isEmpty()) {
            System.out.println(RED + "Nome não pode ser vazio!" + RESET);
            System.out.print(YELLOW + "Digite seu nome: " + RESET);
            this.nomeComprador = scanner.nextLine().trim();
        }
        
        System.out.println();
        System.out.println(GREEN + "Perfil criado com sucesso!" + RESET);
        System.out.println("Nome: " + nomeComprador);
        System.out.println("ID único: " + CYAN + compradorId + RESET);
        System.out.println(YELLOW + "Anote seu ID para consultas futuras!" + RESET);
        System.out.println(GREEN + "Você receberá notificações em tempo real!" + RESET);
        
        pausar();
    }
    
    public void conectarERegistrar() throws RemoteException, NotBoundException {
        System.out.println(CYAN + "\nConectando ao servidor..." + RESET);
        
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            servico = (ServicoLeilao) registry.lookup("ServidorLeilao");
            
            boolean registrado = servico.registrarComprador(compradorId, this);
            
            if (registrado) {
                System.out.println(GREEN + "Conectado e registrado no servidor!" + RESET);
            } else {
                System.out.println(YELLOW + "Você já estava registrado no servidor." + RESET);
            }
        } catch (Exception e) {
            System.out.println(RED + "Erro ao conectar ao servidor!" + RESET);
            System.out.println(YELLOW + "Certifique-se de que o servidor está rodando." + RESET);
            throw e;
        }
    }
    
    public void executarMenu() {
        limparTela();
        System.out.println(CYAN + "╔════════════════════════════════════════════════════════╗" + RESET);
        System.out.println(CYAN + "║        SISTEMA DE LEILÃO - PAINEL DO COMPRADOR        ║" + RESET);
        System.out.println(CYAN + "╚════════════════════════════════════════════════════════╝" + RESET);
        System.out.println(YELLOW + "Comprador: " + RESET + nomeComprador);
        System.out.println(YELLOW + "ID: " + RESET + CYAN + compradorId + RESET);
        System.out.println(GREEN + "Notificações ativas!" + RESET);
        System.out.println();
        
        while (true) {
            try {
                exibirMenu();
                int opcao = lerOpcao();
                
                switch (opcao) {
                    case 1:
                        listarLeiloesAtivos();
                        break;
                    case 2:
                        fazerLance();
                        break;
                    case 3:
                        consultarLeilao();
                        break;
                    case 4:
                        visualizarHistoricoLances();
                        break;
                    case 5:
                        fazerLancesRapidos();
                        break;
                    case 6:
                        exibirMeuPerfil();
                        break;
                    case 0:
                        System.out.println(YELLOW + "\n╔════════════════════════════════════════════════════════╗" + RESET);
                        System.out.println(YELLOW + "║              Encerrando sessão...                     ║" + RESET);
                        System.out.println(YELLOW + "╚════════════════════════════════════════════════════════╝" + RESET);
                        System.out.println("Seu ID: " + CYAN + compradorId + RESET);
                        System.out.println(GREEN + "Obrigado por usar o sistema!" + RESET);
                        return;
                    default:
                        System.out.println(RED + "Opção inválida!" + RESET);
                }
                
                pausar();
                
            } catch (Exception e) {
                System.out.println(RED + "Erro: " + e.getMessage() + RESET);
                e.printStackTrace();
                pausar();
            }
        }
    }
    
    private void exibirMenu() {
        System.out.println("\n" + PURPLE + "═══ MENU PRINCIPAL ═══" + RESET);
        System.out.println("1. " + GREEN + "Listar leilões ativos" + RESET);
        System.out.println("2. " + YELLOW + "Fazer lance em leilão" + RESET);
        System.out.println("3. Consultar leilão específico");
        System.out.println("4. Ver histórico de lances");
        System.out.println("5. " + CYAN + "Modo lances rápidos" + RESET);
        System.out.println("6. Meu perfil");
        System.out.println("0. Sair");
        System.out.print(CYAN + "\nEscolha uma opção: " + RESET);
    }
    
    private int lerOpcao() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    
    private void exibirMeuPerfil() {
        System.out.println("\n" + CYAN + "╔════════════════════════════════════════════════════════╗" + RESET);
        System.out.println(CYAN + "║                   MEU PERFIL                           ║" + RESET);
        System.out.println(CYAN + "╚════════════════════════════════════════════════════════╝" + RESET);
        System.out.println("Nome: " + YELLOW + nomeComprador + RESET);
        System.out.println("ID único: " + CYAN + compradorId + RESET);
        System.out.println("Tipo: " + GREEN + "COMPRADOR" + RESET);
        System.out.println("Notificações: " + GREEN + "ATIVAS" + RESET);
        System.out.println("\n" + YELLOW + "Guarde seu ID para consultas futuras!" + RESET);
        System.out.println(CYAN + "💡 Você receberá alertas quando for superado em leilões" + RESET);
    }
    
    private void listarLeiloesAtivos() throws RemoteException {
        System.out.println("\n" + CYAN + "═══ LEILÕES ATIVOS ═══" + RESET);
        
        List<InfoLeilao> leiloes = servico.listarLeiloesAtivos();
        
        if (leiloes.isEmpty()) {
            System.out.println(YELLOW + "Não há leilões ativos no momento." + RESET);
            System.out.println(CYAN + "Aguarde novos itens serem cadastrados!" + RESET);
            return;
        }
        
        System.out.println("\n" + GREEN + "Total: " + leiloes.size() + " leilão(ões) ativo(s)" + RESET + "\n");
        
        for (int i = 0; i < leiloes.size(); i++) {
            InfoLeilao info = leiloes.get(i);
            boolean isLider = info.getCompradorLiderAtual() != null && 
                             info.getCompradorLiderAtual().equals(compradorId);
            
            System.out.println("┌─────────────────────────────────────────────────────");
            System.out.println("│ " + YELLOW + (i + 1) + ". " + RESET + info.getItem().getNome() + 
                (isLider ? GREEN + " (VOCÊ É O LÍDER!)" + RESET : ""));
            System.out.println("│    " + BLUE + "ID: " + info.getLeilaoId() + RESET);
            System.out.println("│    Descrição: " + info.getItem().getDescricao());
            System.out.println("│    Preço inicial: R$ " + String.format("%.2f", info.getItem().getPrecoInicial()));
            System.out.println("│    " + GREEN + "Lance atual: R$ " + String.format("%.2f", info.getLanceAtual()) + RESET);
            
            double incrementoMinimo = info.getLanceAtual() * 0.05;
            double lanceMinimo = info.getLanceAtual() + incrementoMinimo;
            System.out.println("│    " + YELLOW + "Lance mínimo: R$ " + String.format("%.2f", lanceMinimo) + RESET + 
                " (+5% = R$ " + String.format("%.2f", incrementoMinimo) + ")");
            
            System.out.println("│    Lances: " + info.getQuantidadeLances());
            
            if (info.getCompradorLiderAtual() != null) {
                if (isLider) {
                    System.out.println("│    " + GREEN + "Líder: VOCÊ" + RESET);
                } else {
                    System.out.println("│    " + PURPLE + "Líder: " + info.getCompradorLiderAtual() + RESET);
                }
            } else {
                System.out.println("│    " + YELLOW + "Seja o primeiro a dar lance!" + RESET);
            }
            
            long tempoDecorrido = (System.currentTimeMillis() - info.getTimestampUltimoLance()) / 1000;
            long tempoRestante = 300 - tempoDecorrido;
            
            if (tempoRestante > 0) {
                System.out.println("│    " + RED + "Finaliza em " + tempoRestante + " minutos (sem novos lances)" + RESET);
            }
            
            System.out.println("└─────────────────────────────────────────────────────");
        }
    }
    
    private void fazerLance() throws RemoteException {
        System.out.println("\n" + CYAN + "═══ FAZER LANCE ═══" + RESET);
        
        List<InfoLeilao> leiloes = servico.listarLeiloesAtivos();
        
        if (leiloes.isEmpty()) {
            System.out.println(YELLOW + "Não há leilões ativos no momento." + RESET);
            return;
        }
        
        System.out.println("\n" + YELLOW + "Leilões disponíveis:" + RESET);
        for (int i = 0; i < leiloes.size(); i++) {
            InfoLeilao info = leiloes.get(i);
            System.out.println((i + 1) + ". " + info.getItem().getNome() + 
                " - Lance atual: R$ " + String.format("%.2f", info.getLanceAtual()) +
                " [" + info.getLeilaoId() + "]");
        }
        
        System.out.print("\nDigite o ID do leilão: ");
        String leilaoId = scanner.nextLine().trim();
        
        if (leilaoId.isEmpty()) {
            System.out.println(RED + "ID não pode ser vazio!" + RESET);
            return;
        }
        
        InfoLeilao info;
        try {
            info = servico.consultarLeilao(leilaoId);
        } catch (RemoteException e) {
            System.out.println(RED + "Leilão não encontrado!" + RESET);
            return;
        }
        
        if (info.getCompradorLiderAtual() != null && 
            info.getCompradorLiderAtual().equals(compradorId)) {
            System.out.println(GREEN + "Você já é o líder deste leilão!" + RESET);
            System.out.println("⏳ Aguarde outros compradores darem lances maiores.");
            return;
        }
        
        double lanceMinimo = info.getLanceAtual() * 1.05;
        System.out.println("\nItem: " + YELLOW + info.getItem().getNome() + RESET);
        System.out.println("Lance atual: R$ " + String.format("%.2f", info.getLanceAtual()));
        System.out.println(YELLOW + "Lance mínimo: R$ " + String.format("%.2f", lanceMinimo) + RESET);
        
        System.out.print("\nDigite o valor do seu lance (R$): ");
        double valor;
        try {
            valor = Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println(RED + "Valor inválido!" + RESET);
            return;
        }
        
        if (valor < lanceMinimo) {
            System.out.println(RED + "Lance muito baixo!" + RESET);
            System.out.println("Seu lance: R$ " + String.format("%.2f", valor));
            System.out.println("Mínimo: R$ " + String.format("%.2f", lanceMinimo));
            System.out.println(YELLOW + "Falta: R$ " + String.format("%.2f", lanceMinimo - valor) + RESET);
            return;
        }
        
        System.out.println("\n" + CYAN + "Enviando lance..." + RESET);
        ResultadoLance resultado = servico.fazerLance(leilaoId, compradorId, valor);
        
        System.out.println(GREEN + "Lance enviado com sucesso!" + RESET);
        System.out.println("Valor: R$ " + String.format("%.2f", valor));
        System.out.println(YELLOW + "Aguarde processamento do servidor..." + RESET);
        System.out.println(CYAN + "Você receberá notificação se for superado!" + RESET);
    }
    
    private void consultarLeilao() throws RemoteException {
        System.out.println("\n" + CYAN + "═══ CONSULTAR LEILÃO ═══" + RESET);
        System.out.print("Digite o ID do leilão: ");
        String leilaoId = scanner.nextLine().trim();
        
        if (leilaoId.isEmpty()) {
            System.out.println(RED + "ID não pode ser vazio!" + RESET);
            return;
        }
        
        try {
            InfoLeilao info = servico.consultarLeilao(leilaoId);
            exibirDetalhesLeilao(info);
        } catch (RemoteException e) {
            System.out.println(RED + "Leilão não encontrado!" + RESET);
        }
    }
    
    private void exibirDetalhesLeilao(InfoLeilao info) {
        boolean isLider = info.getCompradorLiderAtual() != null && 
                         info.getCompradorLiderAtual().equals(compradorId);
        String corStatus = obterCorStatus(info.getStatus());
        String iconStatus = obterIconStatus(info.getStatus());
        
        System.out.println("\n╔════════════════════════════════════════════════════════╗");
        System.out.println("║              DETALHES DO LEILÃO                     ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");
        System.out.println("ID: " + CYAN + info.getLeilaoId() + RESET);
        System.out.println("Item: " + YELLOW + info.getItem().getNome() + RESET);
        System.out.println("Descrição: " + info.getItem().getDescricao());
        System.out.println(iconStatus + " Status: " + corStatus + info.getStatus() + RESET);
        System.out.println("Vendedor: " + info.getItem().getVendedorId());
        System.out.println("Preço inicial: R$ " + String.format("%.2f", info.getItem().getPrecoInicial()));
        System.out.println(GREEN + "Lance atual: R$ " + String.format("%.2f", info.getLanceAtual()) + RESET);
        
        if (info.getStatus() == StatusLeilao.ATIVO) {
            double lanceMinimo = info.getLanceAtual() * 1.05;
            System.out.println(YELLOW + "Lance mínimo: R$ " + String.format("%.2f", lanceMinimo) + RESET);
        }
        
        System.out.println("Lances: " + info.getQuantidadeLances());
        
        if (info.getCompradorLiderAtual() != null) {
            if (isLider) {
                System.out.println(GREEN + "Líder: VOCÊ" + RESET);
            } else {
                System.out.println(PURPLE + "Líder: " + info.getCompradorLiderAtual() + RESET);
            }
        } else {
            System.out.println(YELLOW + "Nenhum lance ainda" + RESET);
        }
        
        if (info.getStatus() == StatusLeilao.ATIVO) {
            long tempoDecorrido = (System.currentTimeMillis() - info.getTimestampUltimoLance()) / 1000;
            long tempoRestante = 300 - tempoDecorrido;
            System.out.println("Tempo desde último lance: " + tempoDecorrido + "m");
            
            if (tempoRestante > 0) {
                System.out.println(RED + "Finaliza em " + tempoRestante + "m (sem novos lances)" + RESET);
            }
        }
    }
    
    private void visualizarHistoricoLances() throws RemoteException {
        System.out.println("\n" + CYAN + "═══ HISTÓRICO DE LANCES ═══" + RESET);
        System.out.print("Digite o ID do leilão: ");
        String leilaoId = scanner.nextLine().trim();
        
        if (leilaoId.isEmpty()) {
            System.out.println(RED + "ID não pode ser vazio!" + RESET);
            return;
        }
        
        try {
            List<Lance> lances = servico.listarLancesLeilao(leilaoId);
            InfoLeilao info = servico.consultarLeilao(leilaoId);
            
            if (lances.isEmpty()) {
                System.out.println(YELLOW + "Este leilão ainda não recebeu lances." + RESET);
                return;
            }
            
            System.out.println("\nLeilão: " + YELLOW + info.getItem().getNome() + RESET);
            System.out.println(GREEN + "Total: " + lances.size() + " lance(s)" + RESET + "\n");
            
            for (int i = 0; i < lances.size(); i++) {
                Lance lance = lances.get(i);
                boolean isVencedor = (i == lances.size() - 1);
                boolean isMeuLance = lance.getCompradorId().equals(compradorId);
                
                String prefixo;
                if (isVencedor && info.getStatus() == StatusLeilao.ATIVO) {
                    prefixo = GREEN + " ";
                } else if (isMeuLance) {
                    prefixo = CYAN + ">  ";
                } else {
                    prefixo = "   ";
                }
                
                String sufixo = "";
                if (isVencedor && info.getStatus() == StatusLeilao.ATIVO) {
                    sufixo = " (LÍDER)";
                } else if (isVencedor && info.getStatus() == StatusLeilao.FINALIZADO) {
                    sufixo = " (VENCEDOR)";
                }
                
                if (isMeuLance) {
                    sufixo += " (VOCÊ)";
                }
                
                System.out.println(prefixo + (i + 1) + ". R$ " + 
                    String.format("%.2f", lance.getValor()) + 
                    " - " + lance.getCompradorId() + 
                    sufixo + RESET);
            }
            
        } catch (RemoteException e) {
            System.out.println(RED + "Leilão não encontrado!" + RESET);
        }
    }
    
    private void fazerLancesRapidos() throws RemoteException {
        System.out.println("\n" + CYAN + "═══ MODO LANCES RÁPIDOS ═══" + RESET);
        System.out.println(YELLOW + "Neste modo você pode dar vários lances seguidos" + RESET);
        System.out.println("Digite 'sair' para voltar ao menu principal\n");
        
        while (true) {
            System.out.print(CYAN + "ID do leilão (ou 'sair'): " + RESET);
            String leilaoId = scanner.nextLine().trim();
            
            if (leilaoId.equalsIgnoreCase("sair")) {
                break;
            }
            
            if (leilaoId.isEmpty()) {
                continue;
            }
            
            try {
                InfoLeilao info = servico.consultarLeilao(leilaoId);
                
                System.out.println("Leilão: " + info.getItem().getNome());
                System.out.println("Lance atual: R$ " + String.format("%.2f", info.getLanceAtual()));
                
                double lanceMinimo = info.getLanceAtual() * 1.05;
                System.out.println("Mínimo: R$ " + String.format("%.2f", lanceMinimo));
                
                System.out.print(GREEN + "Seu lance: R$ " + RESET);
                double valor = Double.parseDouble(scanner.nextLine().trim());
                
                servico.fazerLance(leilaoId, compradorId, valor);
                System.out.println(GREEN + "Lance enviado!\n" + RESET);
                
            } catch (NumberFormatException e) {
                System.out.println(RED + "Valor inválido!\n" + RESET);
            } catch (RemoteException e) {
                System.out.println(RED + "Erro: " + e.getMessage() + "\n" + RESET);
            }
        }
    }
    
    private String obterCorStatus(StatusLeilao status) {
        switch (status) {
            case ATIVO: return GREEN;
            case FINALIZADO: return BLUE;
            case CANCELADO: return RED;
            default: return RESET;
        }
    }
    
    private String obterIconStatus(StatusLeilao status) {
        switch (status) {
            case ATIVO: return "🟢";
            case FINALIZADO: return "🔵";
            case CANCELADO: return "🔴";
            default: return "⚪";
        }
    }
    
    private void limparTela() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
    
    private void pausar() {
        System.out.print("\n" + YELLOW + "Pressione ENTER para continuar..." + RESET);
        scanner.nextLine();
        limparTela();
    }
    
    public static void main(String[] args) {
        try {
            ClienteComprador cliente = new ClienteComprador();
            cliente.inicializar();
            cliente.conectarERegistrar();
            
            // Nao e preciso criar Thread para "manter ativo": o menu roda na
            // thread principal e os callbacks chegam pelas threads do runtime RMI.
            cliente.executarMenu();
            
        } catch (Exception e) {
            System.err.println("Erro: " + e.getMessage());
            e.printStackTrace();
        }
    }
}