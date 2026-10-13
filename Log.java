/**
 * Ponto único de registro (log de auditoria) de entrada e saída.
 * System.out.println é sincronizado, então cada linha sai inteira mesmo com várias threads.
 */
public final class Log {

    private Log() { }

    public static void registrar(Veiculo veiculo, String mensagem) {
        System.out.println(veiculo + " " + mensagem);
    }
}
