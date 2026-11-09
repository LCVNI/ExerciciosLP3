import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Gateway TCP do Sistema de Leilão.
 *
 * Permite que compradores participem dos leilões usando apenas um socket TCP
 * com protocolo de texto (uma linha por comando), sem precisar de RMI no cliente.
 *
 *   ClienteSocket  --TCP (texto)-->  GatewaySocket  --RMI-->  ServidorLeiloeiro
 *   ClienteSocket  <--TCP (texto)--  GatewaySocket  <--RMI callback--
 *
 * O gateway é ao mesmo tempo:
 *   - SERVIDOR TCP para os clientes de socket (porta 5000);
 *   - CLIENTE RMI do ServidorLeiloeiro (lookup "ServidorLeilao");
 *   - SERVIDOR RMI de callbacks: cada sessão exporta um CallbackSessao.
 *
 * REGRA DO EXERCÍCIO: nenhuma Thread é criada manualmente (new Thread).
 * Toda concorrência usa ExecutorService.
 *
 * PROTOCOLO (cliente -> gateway):
 *   REGISTRAR <compradorId>
 *   LISTAR
 *   CONSULTAR <leilaoId>
 *   HISTORICO <leilaoId>
 *   LANCE <leilaoId> <valor>        (valor com ponto decimal: 150.00)
 *   SAIR
 *
 * PROTOCOLO (gateway -> cliente):
 *   OK <mensagem>
 *   ERRO <mensagem>
 *   ITEM <texto>                    (linhas de LISTAR/HISTORICO/CONSULTAR)
 *   FIM                             (fim de uma lista)
 *   EVENTO OUTBID <leilaoId> <novoLance> <novoLider>
 *   EVENTO VITORIA <leilaoId> <valorFinal>
 */
public class GatewaySocket {

    public static final int PORTA_GATEWAY = 5000;
    public static final int MAX_SESSOES = 20;

    private final ServicoLeilao servico;
    private final ExecutorService poolSessoes;
    private volatile boolean executando = true;
    private ServerSocket serverSocket;

    public GatewaySocket(ServicoLeilao servico) {
        this.servico = servico;
        this.poolSessoes = Executors.newFixedThreadPool(MAX_SESSOES);
    }

    /**
     * MÉTODO 5 - PARA IMPLEMENTAR
     *
     * Abre o ServerSocket e aceita conexões de clientes.
     *
     * REQUISITOS:
     * 1. Criar o ServerSocket na PORTA_GATEWAY e guardá-lo no atributo serverSocket
     * 2. Enquanto executando:
     *    a) Socket socket = serverSocket.accept()   (bloqueante)
     *    b) Criar new SessaoCliente(socket, servico)
     *    c) Submeter a sessão ao poolSessoes (NÃO usar new Thread)
     *    d) Logar: "CONEXAO ACEITA - <endereço remoto>"
     * 3. Se accept() lançar IOException:
     *    - Se executando == false, é encerramento normal: sair do laço
     *    - Senão, logar o erro e continuar aceitando
     *
     * PERGUNTA PARA REFLETIR:
     * - O pool tem MAX_SESSOES workers. O que acontece com o cliente número
     *   MAX_SESSOES + 1? A conexão é recusada? Fica esperando? Onde?
     */
    public void iniciar() throws IOException {
        // TODO: VOCÊ DEVE IMPLEMENTAR ESTE MÉTODO
        throw new UnsupportedOperationException("Implemente iniciar");
    }

    /**
     * Encerramento gracioso: para de aceitar e interrompe as sessões.
     */
    public void encerrar() {
        executando = false;
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            log("Erro ao fechar ServerSocket: " + e.getMessage());
        }
        poolSessoes.shutdownNow();
        try {
            poolSessoes.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        log("Gateway encerrado");
    }

    static void log(String mensagem) {
        String timestamp = String.format("[%tT]", System.currentTimeMillis());
        System.out.println(timestamp + " [GATEWAY] " + mensagem);
    }

    public static void main(String[] args) {
        String hostRmi = args.length > 0 ? args[0] : "localhost";
        try {
            Registry registry = LocateRegistry.getRegistry(hostRmi, 1099);
            ServicoLeilao servico = (ServicoLeilao) registry.lookup("ServidorLeilao");

            GatewaySocket gateway = new GatewaySocket(servico);

            System.out.println("=== GATEWAY TCP DO LEILÃO ===");
            System.out.println("RMI: " + hostRmi + ":1099  |  TCP: porta " + PORTA_GATEWAY + "\n");
            gateway.iniciar();
        } catch (Exception e) {
            System.err.println("Erro no gateway: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
