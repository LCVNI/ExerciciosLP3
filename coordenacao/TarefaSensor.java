package coordenacao;
import controle.*;
import sensor.*;
import dados.*;
import modelo.Leitura;
import app.*;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;

public class TarefaSensor implements Callable<Integer> {
    CyclicBarrier barreira;
    SensorTemperatura sensorTmp;
    SensorUmidade sensorUmi;
    SensorBase sensorBase;
    CentralMonitoramento central = new CentralMonitoramento();
    EstadoSistema estado = new EstadoSistema();
    Estatisticas stats = new Estatisticas();
    UltimasLeituras ultimas = new UltimasLeituras();
    public TarefaSensor(CyclicBarrier barreira){
        this.barreira = barreira;
    }
    @Override
    public Integer call() throws Exception {
        int contLeituras = 0;
        while(estado.estaAtivo() && stats.getLeituras().get() <= central.getMaxLeituras().get()){

            sensorTmp = new SensorTemperatura(
                (int) Thread.currentThread().threadId()
            );
            sensorUmi = new SensorUmidade(
                (int) Thread.currentThread().threadId()
            );
            processar(sensorTmp.ler());
            contLeituras++;
            processar(sensorUmi.ler());
            contLeituras++;
            boolean parar = aguardarRodada();
            if(parar){break;}
        }
        return contLeituras;
    }

    private void processar(Leitura leitura){
                ultimas.atualizar(leitura);
                stats.registrarLeitura();
                if(sensorUmi.emAlerta(leitura)){stats.registrarAlerta();}
    }

    private boolean aguardarRodada(){
         try {
                barreira.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
                return true;
            }
        return false;
    }
}
