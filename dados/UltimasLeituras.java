package dados;
import modelo.*;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;
public class UltimasLeituras {
    Map<Integer, Leitura> ultimas = Collections.synchronizedMap(new HashMap<Integer,Leitura>());
    public void atualizar(Leitura leitura){
        ultimas.put(leitura.sensorId(), leitura);
    }
    public double mediaPorTipo(TipoSensor tipo){
      double soma = 0;
        int contador = 0;

        synchronized(ultimas) {
            for (Leitura leitura : ultimas.values()) {
                if (leitura.tipo() == tipo) {
                    soma += leitura.valor();
                    contador++;
                }
            }
        }

        if (contador == 0) {
            return 0.0;
        }
        return soma / contador;
    }
}

