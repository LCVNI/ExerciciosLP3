import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

public class Main {
    public volatile static boolean sinalParada = false;
    public static void main(String[] args){
        Leitura sensor1 = new Leitura();
        Leitura sensor2 = new Leitura();
        Leitura sensor3 = new Leitura();
        Painel mostrar = new Painel();
        ExecutorService executor = Executors.newFixedThreadPool(4);

        executor.submit(sensor1);
        executor.submit(sensor2);
        executor.submit(sensor3);
        executor.submit(mostrar);

        try {
            Thread.sleep(200);
            sinalParada = true;
            executor.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Leitura.leituras.isEmpty());

    }
}
