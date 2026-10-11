package tarifa;

import java.util.Random;
import java.util.concurrent.Callable;

public class ServidorTarifa implements Callable<Long> {
    private long tarifa;
    public ServidorTarifa(long tarifa){
        this.tarifa = tarifa;
    }
    @Override
    public Long call() throws Exception {
        String nome = Thread.currentThread().getName();
        Random random = new Random();
        Thread.sleep(random.nextInt((50 - 300) + 1) + 50);
        System.out.println("Tarifa: " + tarifa + " -> Por: " + nome );
        return tarifa;
    }
}
