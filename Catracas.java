import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;

public class Catracas implements Runnable {
    public static final List<String> historicoLogs = Collections.synchronizedList(new ArrayList<>());
    ConcurrentHashMap<String, LongAdder> mapaContadores = new ConcurrentHashMap<>();
    @Override 
    public void run(){
        while(true){
            int valorAnterior = Publico.ingressos.getAndUpdate(atual->{
                if(atual < 200){
                    mapaContadores.computeIfAbsent(Thread.currentThread().getName(),k -> new LongAdder()).increment();
                    historicoLogs.add(Thread.currentThread().getName() + " vendeu ingresso numero "+ atual);
                    return atual + 1;
                }
                else{
                    System.out.println(Thread.currentThread().getName() + " Tentou vender, mas estava lotado");
                    return atual;
                }
            });

            if(valorAnterior < 200){
                System.out.println(Thread.currentThread().getName() + " Comprou ingresso: " + (valorAnterior + 1));
            }
            else{
                System.out.println(Thread.currentThread().getName() + " Nao conseguiu comprar, ingressos esgotados");
                break;
            }


        }
    }
}
