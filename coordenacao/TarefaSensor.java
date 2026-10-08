package coordenacao;
import controle.*;
import sensor.*;
import dados.*;
import modelo.Leitura;
import app.*;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class TarefaSensor implements Callable<Integer> {
    CyclicBarrier barreira;
    Sensor sensor;
    CentralMonitoramento central;
    EstadoSistema estado;
    Estatisticas stats;
    UltimasLeituras ultimas;
    RegistroEventos eventos;
    public TarefaSensor(CyclicBarrier barreira, Sensor sensor, CentralMonitoramento central, EstadoSistema estado, Estatisticas stats, UltimasLeituras ultimas, RegistroEventos eventos){
        this.barreira = barreira;
        this.sensor = sensor;
        this.central = central;
        this.estado = estado;
        this.stats = stats;
        this.ultimas = ultimas;
        this.eventos = eventos;
    }
    @Override
    public Integer call() throws Exception {
        int contLeituras = 0;
        while(estado.estaAtivo() && stats.getLeituras().get() < central.getMaxLeituras()){

            processar(sensor.ler());
            contLeituras++;
            if(aguardarRodada()){break;}
        }
        return contLeituras;
    }

    private void processar(Leitura leitura){
                ultimas.atualizar(leitura);
                stats.registrarLeitura();
                if(sensor.emAlerta(leitura)){
                    stats.registrarAlerta();
                    eventos.registrar("ALERTA: " + leitura);
                }
    }

    private boolean aguardarRodada(){
         try {
                barreira.await(3, TimeUnit.SECONDS);
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
                return true;
            } catch (TimeoutException e) {
                e.printStackTrace();
                return true;
            }
        return false;
    }
}
