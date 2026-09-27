package app;

import java.util.concurrent.atomic.AtomicInteger;

public class CentralMonitoramento {
    private static AtomicInteger maxLeituras = new AtomicInteger();
    public CentralMonitoramento(){

    }

    public AtomicInteger getMaxLeituras(){return maxLeituras;}
}
