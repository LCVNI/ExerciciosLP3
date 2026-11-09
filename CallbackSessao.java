import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Locale;

/**
 * Callback RMI exportado pelo gateway em nome de UM cliente de socket.
 *
 * O ServidorLeiloeiro chama aoSerSuperado/aoVencerLeilao via RMI; este objeto
 * converte a chamada em uma linha de texto e escreve no socket do cliente.
 *
 * ATENÇÃO À CONCORRÊNCIA:
 * O mesmo PrintWriter é usado por DUAS threads diferentes:
 *   - o worker do poolSessoes, que responde aos comandos (OK/ERRO/ITEM/FIM);
 *   - uma thread do runtime RMI, que entrega os callbacks (EVENTO ...).
 * Sem proteção, linhas de respostas e eventos podem se misturar no socket.
 */
public class CallbackSessao extends UnicastRemoteObject implements CallbackComprador {

    private final PrintWriter saida;

    public CallbackSessao(PrintWriter saida) throws RemoteException {
        super();
        this.saida = saida;
    }

    /**
     * MÉTODO 7 - PARA IMPLEMENTAR
     *
     * Envia UMA linha ao cliente de forma thread-safe.
     *
     * REQUISITOS:
     * 1. Garantir que duas threads nunca escrevam ao mesmo tempo no PrintWriter
     *    (synchronized, ReentrantLock ou outra estratégia; justifique a escolha)
     * 2. Escrever a linha e dar flush
     * 3. Retornar false se o socket já estiver com erro (saida.checkError()),
     *    para que o chamador saiba que o cliente caiu
     *
     * TODA escrita da sessão (respostas e eventos) deve passar por este método.
     */
    public boolean enviar(String linha) {
        // TODO: VOCÊ DEVE IMPLEMENTAR ESTE MÉTODO
        throw new UnsupportedOperationException("Implemente enviar");
    }

    /**
     * Repassa o outbid ao cliente de socket.
     * Se o socket caiu, lança RemoteException: o servidor já trata isso
     * e apenas registra em log ("FALHA CALLBACK OUTBID").
     */
    @Override
    public void aoSerSuperado(String leilaoId, double novoLance, String novoLider) throws RemoteException {
        String linha = String.format(Locale.US, "EVENTO OUTBID %s %.2f %s", leilaoId, novoLance, novoLider);
        if (!enviar(linha)) {
            throw new RemoteException("Cliente de socket desconectado");
        }
    }

    @Override
    public void aoVencerLeilao(String leilaoId, double valorFinal) throws RemoteException {
        String linha = String.format(Locale.US, "EVENTO VITORIA %s %.2f", leilaoId, valorFinal);
        if (!enviar(linha)) {
            throw new RemoteException("Cliente de socket desconectado");
        }
    }
}
