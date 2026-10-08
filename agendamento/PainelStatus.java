package agendamento;
import dados.Estatisticas;
import controle.EstadoSistema;
public class PainelStatus implements Runnable {
    Estatisticas stats;
    EstadoSistema sistema;
    public PainelStatus(Estatisticas stats, EstadoSistema sistema){
        this.sistema = sistema;
        this.stats = stats;
    }
    @Override
    public void run(){
        System.out.println("Rodadas: "+stats.getRodadas().get());
        System.out.println("Leituras: "+stats.getLeituras().get());
        System.out.println("Alertas: "+stats.getAlertas().get());
        System.out.println("Sistema esta ativo: "+sistema.estaAtivo());
    }
}
