import java.util.concurrent.atomic.AtomicInteger;

public class Veiculo {
    private static final AtomicInteger contador = new AtomicInteger(0);
    private final int id;
    private final TipoVeiculo tipo;
    private final long chegada;
    
    public Veiculo(TipoVeiculo tipo) {
        this.id = contador.incrementAndGet();
        this.tipo = tipo;
        this.chegada = System.currentTimeMillis();
    }
    
    public int getId() { 
        return id; 
    }
    public TipoVeiculo getTipo() { 
        return tipo; 
    }
    public long getChegada() { 
        return chegada; 
    }
    
    @Override
    public String toString() {
        return String.format("Veículo #%d (%s)", id, tipo);
    }
}
