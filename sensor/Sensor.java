package sensor;
import modelo.*;
public interface Sensor {
    int getId();
    TipoSensor getTipo();
    Leitura ler() throws InterruptedException;
    boolean emAlerta(Leitura leitura);
}
