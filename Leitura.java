import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.ArrayBlockingQueue;

public class Leitura implements Runnable {
    public static BlockingQueue<Integer> leituras = new ArrayBlockingQueue<>(50);
    @Override 
    public void run(){
        while(!Main.sinalParada){
            int valor = ThreadLocalRandom.current().nextInt(0,200);
            leituras.add(valor);
            System.out.println(Thread.currentThread().getName() + " adicionou " + valor);
        }
        if(Main.sinalParada){
                System.out.println("Recebi sinal de parada! -- FINALIZANDO... (" + Thread.currentThread().getName()+")");
            }
    }
}
