import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.text.NumberFormat;
public class Main {
     public static void main(String[] args){
        Locale localBrasil = Locale.forLanguageTag("pt-BR");
        NumberFormat formatadorMoeda = NumberFormat.getCurrencyInstance(localBrasil);

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Pedido pedido1 = new Pedido(1, "Ana" , 120.0);
        Pedido pedido2 = new Pedido(2, "Bruno" , 350.0);
        Pedido pedido3 = new Pedido(3, "Carlos" , 80.0);
        Pedido pedido4 = new Pedido(4, "Diana" , 450.0);
        Pedido pedido5 = new Pedido(5, "Eduardo" , 200.0);

        List<Future<String>> listaRetornos = new ArrayList<>();
        ArrayList<Pedido> pedidos = new ArrayList<>();
        pedidos.add(pedido1);
        pedidos.add(pedido2);
        pedidos.add(pedido3);
        pedidos.add(pedido4);
        pedidos.add(pedido5);

        for(Pedido p: pedidos){
            Future<String> retorno = executor.submit(new ProcessarPedido(p));
            listaRetornos.add(retorno);
        }
        ArrayList<String> consumidos = new ArrayList<>();
        for(Future<String> r: listaRetornos){
            try {
                consumidos.add(r.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        //Finalizar threads
        executor.shutdown();
        try {
            executor.awaitTermination(100, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Imprimir retorno das threads
        for(String c: consumidos){
            System.out.println(c);
        }

        //Imprimir informacoes
        double faturamentoTotal = ProcessarPedido.totalCentavos.get() / 100.0;
        String faturamentoFormatado = formatadorMoeda.format(faturamentoTotal);
        System.out.println("Toral pedidos processados: " + ProcessarPedido.totalProcesssados);
        System.out.println("Valor total processado: " + faturamentoFormatado);
        System.out.println("Pedidos processados: " + ProcessarPedido.pedidosProcessados);
    }
}   
