package coordenacao;
//import java.util.ArrayList;
import java.util.concurrent.CyclicBarrier;
import dados.*;
import modelo.TipoSensor;
import controle.*;
public class ConsolidadorRodada implements Runnable{
    Estatisticas stats = new Estatisticas();
    UltimasLeituras ultimas = new UltimasLeituras();
    @Override
    public void run() {
        CyclicBarrier barreira = new CyclicBarrier(3, this);
        //ArrayList<Double> medias = new ArrayList<>();
        System.out.println("Media por tipo de sensor:");
        for(TipoSensor t: TipoSensor.values()){
            //medias.add(ultimas.mediaPorTipo(t));
            System.out.println(t + ": "+ ultimas.mediaPorTipo(t)+t.getUnidade());
        }
        System.out.println("Thread atual: " + Thread.currentThread().getName());
        PoliticaDesligamento politica = new PoliticaDesligamento(10);
        politica.deveDesligar(stats);
        new TarefaSensor(barreira);
    }
    
}
