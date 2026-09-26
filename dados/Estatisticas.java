package dados;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class Estatisticas {
    private static AtomicLong leituras = new AtomicLong();
    private static AtomicInteger alertas = new AtomicInteger();
    private static AtomicInteger rodadas = new AtomicInteger();

    public void registrarLeitura(){
        leituras.incrementAndGet();
    }
    public void registrarAlerta(){
        alertas.incrementAndGet();
    }
    public int proximaRodada(){
        return rodadas.incrementAndGet();
    }
    //Getters
    public AtomicLong getLeituras(){return leituras;}
    public AtomicInteger getAlertas(){return alertas;}
    public AtomicInteger getRodadas(){return rodadas;}
}
