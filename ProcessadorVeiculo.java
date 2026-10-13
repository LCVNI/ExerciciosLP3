import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Ciclo de vida de um veículo: aguarda a largada, entra, permanece e sai.
 * Retorna o tempo total (entrada até saída) em ms, ou -1 se desistiu.
 */
public class ProcessadorVeiculo implements Callable<Long> {

    private static final long PERMANENCIA_MIN_MS = 1000;
    private static final long PERMANENCIA_MAX_MS = 5000;

    private final Veiculo veiculo;
    private final Estacionamento estacionamento;
    private final CountDownLatch largada;   // libera todos os veículos ao mesmo tempo
    private final CountDownLatch conclusao; // avisa a main que este veículo terminou

    public ProcessadorVeiculo(Veiculo veiculo, Estacionamento estacionamento,
                              CountDownLatch largada, CountDownLatch conclusao) {
        this.veiculo = veiculo;
        this.estacionamento = estacionamento;
        this.largada = largada;
        this.conclusao = conclusao;
    }

    @Override
    public Long call() throws Exception {
        // TODO 9:
        // 1. Aguardar o sinal de largada (largada.await())
        // 2. Executar e retornar processarCiclo()
        // 3. Em um finally: conclusao.countDown()
        //    (deve contar mesmo se o veículo desistir ou ocorrer exceção)
        return -1L; // placeholder
    }

    private long processarCiclo() throws InterruptedException {
        // TODO 10:
        // 1. Marcar o instante de início
        // 2. Tentar entrar; se o Optional estiver vazio, retornar -1
        // 3. Em try: permanecer()
        //    Em finally: sair(...)  (a vaga deve ser devolvida mesmo se houver interrupção)
        // 4. Retornar o tempo decorrido desde o início
        return -1L; // placeholder
    }

    private void permanecer() throws InterruptedException {
        // TODO 11:
        // - Sortear um tempo entre PERMANENCIA_MIN_MS e PERMANENCIA_MAX_MS (inclusive)
        //   Dica: ThreadLocalRandom.current().nextLong(min, max + 1)
        // - Log: "está estacionado por <tempo>ms"
        // - Thread.sleep(tempo)
    }
}
