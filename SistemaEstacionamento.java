import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.LongSummaryStatistics;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class SistemaEstacionamento {

    private static final int NUM_VEICULOS = 20;
    private static final int NUM_THREADS = 4;
    private static final double PROB_PRIORITARIO = 0.3;

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Sistema de Estacionamento Inteligente ===\n");

        // TODO 12: Implementar o fluxo principal
        // 1. Criar o Estacionamento e gerar a lista de veículos (gerarVeiculos)
        // 2. Criar os latches:
        //      largada   = new CountDownLatch(1)
        //      conclusao = new CountDownLatch(<quantidade de veículos>)
        // 3. Criar o ExecutorService (FixedThreadPool com NUM_THREADS)
        // 4. Dentro de um try:
        //      - submeterVeiculos(...)
        //      - imprimir "\n=== INICIANDO SIMULAÇÃO ===\n"
        //      - largada.countDown()   -> dispara todos os veículos
        //      - conclusao.await()     -> espera o último terminar
        //      - coletarTempos, exibirStatus, exibirEstatisticas
        // 5. No finally: encerrar(executor)

        System.out.println("\n=== Simulação Concluída ===");
    }

    // ==================== ETAPAS DA SIMULAÇÃO ====================

    private static List<Veiculo> gerarVeiculos(int quantidade) {
        System.out.println("=== GERANDO VEÍCULOS ===\n");
        List<Veiculo> veiculos = new ArrayList<>();
        // TODO 13: gerar 'quantidade' veículos com gerarVeiculoAleatorio(),
        //          adicionar à lista e imprimir "Gerado: <veículo>"
        return veiculos;
    }

    /** Gera um veículo aleatório (70% normal, 30% prioritário). */
    private static Veiculo gerarVeiculoAleatorio() {
        TipoVeiculo tipo = ThreadLocalRandom.current().nextDouble() < PROB_PRIORITARIO
                ? TipoVeiculo.PRIORITARIO : TipoVeiculo.NORMAL;
        return new Veiculo(tipo);
    }

    private static List<Future<Long>> submeterVeiculos(ExecutorService executor, List<Veiculo> veiculos,
                                                       Estacionamento estacionamento,
                                                       CountDownLatch largada, CountDownLatch conclusao) {
        List<Future<Long>> futures = new ArrayList<>();
        // TODO 14: para cada veículo, submeter um ProcessadorVeiculo ao executor
        //          e guardar o Future retornado
        return futures;
    }

    /** Recolhe os tempos de quem completou o ciclo (descarta desistências, -1). */
    private static List<Long> coletarTempos(List<Future<Long>> futures) throws InterruptedException {
        List<Long> tempos = new ArrayList<>();
        // TODO 15: para cada Future, chamar get();
        //   - adicionar o tempo apenas se for >= 0
        //   - tratar ExecutionException imprimindo o erro em System.err
        return tempos;
    }

    private static void encerrar(ExecutorService executor) throws InterruptedException {
        // TODO 16: shutdown(); se awaitTermination(10 s) retornar false, shutdownNow()
    }

    // ==================== ESTATÍSTICAS ====================

    private static void exibirEstatisticas(List<Long> tempos, Estacionamento estacionamento) {
        System.out.println("\n=== ESTATÍSTICAS FINAIS ===");
        if (tempos.isEmpty()) {
            System.out.println("Nenhum veículo completou o ciclo.");
            return;
        }
        // TODO 17: exibir
        //   - Total de veículos (entradas + desistências)
        //   - Conseguiram entrar / Desistiram
        //   - Taxa de sucesso           (formatarPercentual)
        //   - Tempo médio, mínimo, máximo (formatarTempo)
        //     Dica: tempos.stream().mapToLong(Long::longValue).summaryStatistics()
        //   - Taxa de ocupação = veículos que estacionaram / veículos que chegaram
        System.out.println("===========================");
    }

    private static String formatarPercentual(int parte, int total) {
        return String.format(Locale.ROOT, "%.1f%%", 100.0 * parte / total);
    }

    private static String formatarTempo(double ms) {
        return String.format(Locale.ROOT, "%.2f ms (%.2f segundos)", ms, ms / 1000.0);
    }

    private static String formatarTempo(long ms) {
        return String.format(Locale.ROOT, "%d ms (%.2f segundos)", ms, ms / 1000.0);
    }
}
