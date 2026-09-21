import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.text.NumberFormat;
public class Main {

    public static volatile boolean expedienteEncerraado = false;
     public static void main(String[] args){
        Locale localBrasil = Locale.forLanguageTag("pt-BR");
        NumberFormat formatadorMoeda = NumberFormat.getCurrencyInstance(localBrasil);

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Nota pedido1 = new Nota(1, "Ana" , 120.0, "BA");
        Nota pedido2 = new Nota(2, "Bruno" , 350.0, "SP");
        Nota pedido3 = new Nota(3, "Carlos" , 80.0, "BA");
        Nota pedido4 = new Nota(4, "Diana" , 450.0, "RJ");
        Nota pedido5 = new Nota(5, "Eduardo" , 200.0, "SP");
        Nota pedido6 = new Nota(6, "Fernanda", 300.0, "BA");
        Nota pedido7 = new Nota(7, "Gabriel", 150.0, "RJ");
        Nota pedido8 = new Nota(8, "Helena", 90.0, "SP");
        Nota pedido9 = new Nota(9, "Igor", 500.0, "BA");
        Nota pedido10 = new Nota(10, "Julia", 60.0, "RJ");
        List<Future<String>> listaRetornos = new ArrayList<>();
        ArrayList<Nota> notas = new ArrayList<>();
        notas.add(pedido1);
        notas.add(pedido2);
        notas.add(pedido3);
        notas.add(pedido4);
        notas.add(pedido5);
        notas.add(pedido6);
        notas.add(pedido7);
        notas.add(pedido8);
        notas.add(pedido9);
        notas.add(pedido10);
        
        //Instancia e inicia a thread do painel
        Thread painel = new Thread( () -> {
            while (!expedienteEncerraado) {
                try {
                    Thread.sleep(165);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Notas emitidas: " + EmitirNota.emitidas);
                System.out.println("Notas Canceladas: " + EmitirNota.canceladas);
                double acumulado = EmitirNota.totalCentavosnota.get() / 100.0;
                String acumuladoFormatado = formatadorMoeda.format(acumulado);
                System.out.println("Acumulado: " + acumuladoFormatado);
                System.out.println("------------------------------------------------------------------");
            }   
        });
        painel.setDaemon(true);
        painel.start();


        //Laço que inicia as threads de emissao de notas
        for(Nota n: notas){
            Future<String> retorno = executor.submit(new EmitirNota(n));
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
        expedienteEncerraado = true;
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
        double totalEmitido = EmitirNota.totalCentavosnota.get() / 100.0;
        String emitidoFormatado = formatadorMoeda.format(totalEmitido);
        System.out.println("Notas emitidas: " + EmitirNota.emitidas);
        System.out.println("Notas canceladas: " + EmitirNota.canceladas);
        System.out.println("Valor emitido: " + emitidoFormatado);
        System.out.println("Parada acionada: " + EmitirNota.paradaSolicitada);
        Map<String, AtomicInteger> notasOrdenado = new TreeMap<>(EmitirNota.notasPorUf);
        System.out.println("Notas por UF: "+ notasOrdenado);

        //Imprimir eventos
        System.out.println("Log de auditoria ("+ EmitirNota.log.size() + " eventos):");
        for(String n: EmitirNota.log){
            System.out.println(n);
        }
    }
}   
