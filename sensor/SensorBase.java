package sensor;
import modelo.*;
import java.util.concurrent.ThreadLocalRandom;
public abstract class SensorBase implements Sensor {
    private int id;
    private TipoSensor tipo;
    public SensorBase(int id, TipoSensor tipo){
        this.id = id;
        this.tipo = tipo;
    }
    @Override 
    public int getId(){return id;}
    public TipoSensor getTipo(){return tipo;}
    protected abstract double gerarValor();
    protected abstract double limiteAlerta();

    public final Leitura ler() throws InterruptedException{
        long dormir = ThreadLocalRandom.current().nextLong(100, 400 + 1);
        Thread.sleep(dormir);
        Leitura leitura = new Leitura(getId(), getTipo(), gerarValor());
        return leitura; 
    }

    @Override
    public boolean emAlerta(Leitura leitura) {
        if(leitura.valor() > limiteAlerta()){
            return true;
        }
        return false;
    }
}
