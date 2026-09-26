package sensor;

import java.util.concurrent.ThreadLocalRandom;

import modelo.TipoSensor;

public class SensorTemperatura extends SensorBase {
    public SensorTemperatura(int id){
        super(id, TipoSensor.TEMPERATURA);
    }
    @Override
    protected double gerarValor() {
        return ThreadLocalRandom.current().nextDouble(30, 40);
    }
    protected double limiteAlerta() {
        return 38.0;
    }
}
