import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Cliente comprador "leve": fala com o GatewaySocket por TCP, sem RMI.
 *
 * Precisa fazer duas coisas ao mesmo tempo:
 *   - ler o teclado e enviar comandos (thread principal);
 *   - receber do gateway respostas E eventos assíncronos (OUTBID, VITORIA).
 * A recepção roda em um ExecutorService de uma única thread (não new Thread).
 *
 * Também funciona com:  nc localhost 5000
 */
public class ClienteSocket {

    public static void main(String[] args) {
        String host = args.length > 0 ? args[0] : "localhost";
        int porta = args.length > 1 ? Integer.parseInt(args[1]) : GatewaySocket.PORTA_GATEWAY;

        ExecutorService receptor = Executors.newSingleThreadExecutor();

        try (Socket socket = new Socket(host, porta);
             BufferedReader entrada = new BufferedReader(
                 new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
             PrintWriter saida = new PrintWriter(socket.getOutputStream(), true, StandardCharsets.UTF_8);
             Scanner teclado = new Scanner(System.in)) {

            receptor.submit(() -> {
                try {
                    String linha;
                    while ((linha = entrada.readLine()) != null) {
                        if (linha.startsWith("EVENTO OUTBID")) {
                            System.out.println("\n>>> VOCE FOI SUPERADO: " + linha.substring(14));
                        } else if (linha.startsWith("EVENTO VITORIA")) {
                            System.out.println("\n>>> VOCE VENCEU: " + linha.substring(15));
                        } else {
                            System.out.println(linha);
                        }
                    }
                    System.out.println("Conexao encerrada pelo gateway.");
                } catch (IOException e) {
                    System.out.println("Conexao perdida: " + e.getMessage());
                }
            });

            while (teclado.hasNextLine()) {
                String comando = teclado.nextLine();
                saida.println(comando);
                if (comando.trim().equalsIgnoreCase("SAIR")) {
                    break;
                }
            }
            // Espera o receptor ler as ultimas linhas antes de fechar o socket
            receptor.shutdown();
            receptor.awaitTermination(2, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (IOException e) {
            System.err.println("Nao foi possivel conectar ao gateway: " + e.getMessage());
        } finally {
            receptor.shutdownNow();
        }
    }
}
