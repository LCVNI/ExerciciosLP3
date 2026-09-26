package controle;
import dados.*;
public class PoliticaDesligamento {
   int maxAlertas;
    public PoliticaDesligamento(int maxAlertas){
        this.maxAlertas = maxAlertas;
    }
    public boolean deveDesligar(Estatisticas stats){
        if(stats.getAlertas().get() >= maxAlertas){
            return true;
        }
        return false;
    }
}
