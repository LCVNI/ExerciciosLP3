import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.List;
public class EmitirNota implements Callable<String>{
    static  volatile boolean paradaSolicitada = false;
    public static AtomicInteger totalCentavosnota = new AtomicInteger();
    public static AtomicInteger emitidas = new AtomicInteger();
    public static AtomicInteger canceladas = new AtomicInteger();
    public static List<String> log = new ArrayList<>();
    public static ConcurrentHashMap<String, AtomicInteger> notasPorUf = new ConcurrentHashMap<>();
    Nota nota;
    public EmitirNota(Nota nota){
        this.nota = nota;
    }

    @Override 
    public String call(){
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(paradaSolicitada){
            canceladas.incrementAndGet();
            log.add("Nota " + nota.id + " CANCELADA antes de iniciar");
            return "Nota " + nota.id + " cancelada (limite diario atingido)";
        }
        int centavosDaNota = (int) Math.round(nota.valor * 100);
        if(totalCentavosnota.addAndGet(centavosDaNota) >= (1000 * 100)){
            log.add("PARADA acionada na nota " + nota.id + ": limite diario atingido");
            paradaSolicitada = true;
        }
        emitidas.incrementAndGet();
        notasPorUf.computeIfAbsent(nota.uf, k -> new AtomicInteger(0)).incrementAndGet();
        log.add("Nota "+ nota.id + " EMITIDA (" + nota.uf + ")" );
        return "Nota " + nota.id + " emitida com sucesso";
    }
}
