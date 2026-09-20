import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

public class ProcessarPedido implements Callable<String> {
    Pedido pedido;
    public static AtomicInteger totalProcesssados = new AtomicInteger();
    public static AtomicInteger totalCentavos = new AtomicInteger();
    public static List<Integer> pedidosProcessados = Collections.synchronizedList(new ArrayList<>());

    public ProcessarPedido(Pedido pedido){
        this.pedido = pedido;
    }
 
    @Override
    public String call() throws Exception {
        System.out.println("Iniciando pedido " + pedido.id + " " + pedido.cliente + " na " + Thread.currentThread().getName());
        Thread.sleep(2000);
        totalProcesssados.incrementAndGet();
        int centavosDoPedido = (int) Math.round(pedido.valor * 100);
        totalCentavos.addAndGet(centavosDoPedido);
        pedidosProcessados.add(pedido.id);
        System.out.println("Finalizando pedido " + pedido.id);
        return "Pedido " + pedido.id + " processado com sucesso" ;
    }
    
}