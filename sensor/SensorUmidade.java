package sensor;

import java.util.concurrent.ThreadLocalRandom;

import modelo.TipoSensor;

public class SensorUmidade extends SensorBase {
    public SensorUmidade(int id){
        super(id, TipoSensor.UMIDADE);
    }

    @Override
    protected double gerarValor() {
        return ThreadLocalRandom.current().nextDouble(40, 95);
    }
    protected double limiteAlerta() {
        return 85.0;
    }
}
