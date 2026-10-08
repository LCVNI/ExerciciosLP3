package agendamento;

import java.io.Closeable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Agendador implements Closeable {
    private final ScheduledExecutorService executor;
    private final List<ScheduledFuture<?>> agendados;
    public Agendador(){
        this.executor = Executors.newScheduledThreadPool(5);
        this.agendados = new ArrayList<>();
    }
    public void aTaxaFixa(Runnable execucao, long inicialMs, long periodoMs){
        ScheduledFuture<?> comprovante = executor.scheduleAtFixedRate(execucao, inicialMs, periodoMs, TimeUnit.MILLISECONDS);
        this.agendados.add(comprovante);
    }
    public void comAtrasoFixo(Runnable execucao, long inicialMs, long atrasoMs){
        ScheduledFuture<?> comprovante = executor.scheduleWithFixedDelay(execucao, inicialMs, atrasoMs, TimeUnit.MILLISECONDS);
        this.agendados.add(comprovante);
    }
    @Override 
    public void close(){
        for(ScheduledFuture<?> a: this.agendados){
            if(!a.isDone()){
                a.cancel(true);
            }
        }
        executor.shutdown();
        try {
            executor.awaitTermination(2, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }

    }
}
