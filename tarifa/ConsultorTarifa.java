package tarifa;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

public class ConsultorTarifa {
    private ExecutorService executor;
    List<ServidorTarifa> servidores = new ArrayList<>(); 
    public ConsultorTarifa(ExecutorService executor, List<ServidorTarifa> servidores){
        this.executor = executor;
        this.servidores = servidores;
    }

    public long consultarMaisRapido(){
        long servidor = 0;
        try {
            servidor = executor.invokeAny(servidores);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return servidor;
    }

}
