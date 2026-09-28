package coordenacao;
//import java.util.ArrayList;
import dados.*;
import modelo.TipoSensor;
import controle.*;
public class ConsolidadorRodada implements Runnable{
    Estatisticas stats;
    UltimasLeituras ultimas;
    EstadoSistema estado;
    public ConsolidadorRodada(Estatisticas stats, UltimasLeituras ultimas, EstadoSistema estado){
        this.stats = stats;
        this.ultimas = ultimas;
        this.estado = estado;
    }
    @Override
    public void run() {
        int rodada = stats.proximaRodada();
        //ArrayList<Double> medias = new ArrayList<>();
        System.out.println("Media por tipo de sensor:");
        for(TipoSensor t: TipoSensor.values()){
            //medias.add(ultimas.mediaPorTipo(t));
            System.out.println(t + ": "+ ultimas.mediaPorTipo(t)+t.getUnidade());
        }
        System.out.println("Thread atual: " + Thread.currentThread().getName());
        System.out.println("Rodada atual: " + rodada);
        PoliticaDesligamento politica = new PoliticaDesligamento(10);
        if (politica.deveDesligar(stats)) {
            estado.desligar();
        }
    }
    
}
