public class Main {
    public static void main(String[] args) {
        for (int i = 1; i <= 5; i++) {
            // 1. Cria o nome dinâmico para diferenciar cada uma
            String identificador = "Vendedor-" + i;

            // 2. Passa o nome para a outra classe
            Runnable tarefa = new Vendedor(identificador);

            // 3. Instancia a Thread (também podemos dar o nome oficial dela na JVM)
            Thread t = new Thread(tarefa, identificador);

            // 4. Inicia a thread
            t.start();
        }
    }
}
