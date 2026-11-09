import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Locale;

/**
 * Atende UM cliente de socket. Executada por um worker do poolSessoes
 * do GatewaySocket (é um Runnable, não uma Thread).
 */
public class SessaoCliente implements Runnable {

    private final Socket socket;
    private final ServicoLeilao servico;
    private CallbackSessao callback;
    private String compradorId;   // null até o comando REGISTRAR

    public SessaoCliente(Socket socket, ServicoLeilao servico) {
        this.socket = socket;
        this.servico = servico;
    }

    @Override
    public void run() {
        try (BufferedReader entrada = new BufferedReader(
                 new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
             PrintWriter saida = new PrintWriter(socket.getOutputStream(), false, StandardCharsets.UTF_8)) {

            callback = new CallbackSessao(saida);
            callback.enviar("OK Conectado ao gateway. Comandos: REGISTRAR, LISTAR, CONSULTAR, HISTORICO, LANCE, SAIR");

            String linha;
            while ((linha = entrada.readLine()) != null) {
                linha = linha.trim();
                if (linha.isEmpty()) {
                    continue;
                }
                if (!processarComando(linha)) {
                    break; // SAIR
                }
            }
        } catch (IOException e) {
            GatewaySocket.log("SESSAO ENCERRADA COM ERRO - " + e.getMessage());
        } finally {
            encerrar();
        }
    }

    /**
     * MÉTODO 6 - PARA IMPLEMENTAR
     *
     * Interpreta uma linha do protocolo e chama o ServidorLeiloeiro via RMI.
     * Retorna false apenas para SAIR (encerra a sessão); true nos demais casos.
     *
     * REQUISITOS:
     * 1. Separar a linha em partes: linha.split("\\s+")
     * 2. Tratar cada comando:
     *    REGISTRAR <id>
     *       - Se a sessão já tem compradorId, responder ERRO
     *       - servico.registrarComprador(id, callback)
     *       - true  -> guardar compradorId e responder "OK REGISTRADO <id>"
     *       - false -> responder "ERRO ID ja registrado"
     *    LISTAR
     *       - servico.listarLeiloesAtivos(); uma linha "ITEM ..." por leilão; depois "FIM"
     *    CONSULTAR <leilaoId>
     *       - servico.consultarLeilao(leilaoId); "ITEM ..." e "FIM"
     *    HISTORICO <leilaoId>
     *       - servico.listarLancesLeilao(leilaoId); "ITEM ..." por lance e "FIM"
     *    LANCE <leilaoId> <valor>
     *       - Exigir REGISTRAR antes (senão ERRO)
     *       - Converter valor com Double.parseDouble (NumberFormatException -> ERRO)
     *       - servico.fazerLance(leilaoId, compradorId, valor)
     *       - Responder "OK " + resultado.getMensagem()
     *         (o resultado final chega depois, como EVENTO, via callback)
     *    SAIR
     *       - Responder "OK Tchau" e retornar false
     *    Qualquer outro
     *       - Responder "ERRO Comando desconhecido"
     * 3. Toda RemoteException vira "ERRO <mensagem>" e a sessão CONTINUA
     * 4. Toda escrita deve usar callback.enviar(...)
     */
    private boolean processarComando(String linha) {
        String[] p = linha.split("\\s+");
        String cmd = p[0].toUpperCase(Locale.ROOT);

        try {
            switch (cmd) {
                // EXEMPLO PRONTO: siga este modelo para os demais comandos
                case "LISTAR": {
                    List<InfoLeilao> ativos = servico.listarLeiloesAtivos();
                    for (InfoLeilao info : ativos) {
                        callback.enviar("ITEM " + info);
                    }
                    callback.enviar("FIM");
                    break;
                }

                // TODO: REGISTRAR, CONSULTAR, HISTORICO, LANCE, SAIR

                default:
                    callback.enviar("ERRO Comando desconhecido: " + p[0]);
            }
        } catch (RemoteException e) {
            callback.enviar("ERRO " + e.getMessage());
        }
        return true;
    }

    /**
     * MÉTODO 8 - PARA IMPLEMENTAR
     *
     * Libera os recursos da sessão. Chamado SEMPRE (finally do run).
     *
     * REQUISITOS:
     * 1. Se callback != null, retirar o objeto do RMI:
     *    UnicastRemoteObject.unexportObject(callback, true)
     *    (NoSuchObjectException pode ser ignorada)
     * 2. Fechar o socket (IOException apenas logada)
     * 3. Logar: "SESSAO FINALIZADA - Comprador=<id ou anonimo>"
     *
     * PERGUNTA PARA REFLETIR:
     * - O comprador continua no mapa do servidor depois que o socket cai.
     *   O que acontece quando o servidor tentar notificá-lo?
     *   Isso derruba o servidor? Onde isso é tratado?
     */
    private void encerrar() {
        // TODO: VOCÊ DEVE IMPLEMENTAR ESTE MÉTODO
        throw new UnsupportedOperationException("Implemente encerrar");
    }
}
