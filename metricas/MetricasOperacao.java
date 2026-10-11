package metricas;

import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class MetricasOperacao {
    private final AtomicInteger aprovadas = new AtomicInteger();
    private final AtomicInteger recusadas = new AtomicInteger();
    private final AtomicLong tarifas = new AtomicLong();

    public void registrarAprovada(){
        aprovadas.incrementAndGet();
    }

    public void rgistarRecusada(){
        recusadas.incrementAndGet();
    }

    public AtomicInteger getAprovadas() {return aprovadas;}
    public AtomicInteger getRecusadas() {return recusadas;}
    public AtomicLong getTarifas() {return tarifas;
}
}
