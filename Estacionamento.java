import java.util.Optional;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Estacionamento {

    public static final int VAGAS_REGULARES = 5;
    public static final int VAGAS_PRIORITARIAS = 2;
    private static final long TIMEOUT_VAGA_MS = 2000;

    private final Semaphore vagasRegulares = new Semaphore(VAGAS_REGULARES, true);
    private final Semaphore vagasPrioritarias = new Semaphore(VAGAS_PRIORITARIAS, true);
    private final Semaphore portaoEntrada = new Semaphore(1, true);
    private final Semaphore portaoSaida = new Semaphore(VAGAS_PRIORITARIAS, true);

    private final AtomicInteger totalEntradas = new AtomicInteger(0);
    private final AtomicInteger totalDesistencias = new AtomicInteger(0);

    // ==================== ENTRADA ====================

    /**
     * Passa pelo portão de entrada e tenta obter uma vaga.
     * @return a vaga ocupada, ou Optional.empty() se o veículo desistiu
     */
    public Optional<TipoVaga> tentarEntrar(Veiculo veiculo) throws InterruptedException {
        portaoEntrada.acquire();
        TipoVaga vaga = null;
        try{
            Log.registrar(veiculo, "chegou ao portão de entrada");
            alocarVaga(veiculo);
            if(ehPrioritario(veiculo)){
                vaga = TipoVaga.PRIORITARIA;
                registrarResultadoEntrada(veiculo, Optional.of(vaga));
            }
            else{
                vaga = TipoVaga.REGULAR;
                registrarResultadoEntrada(veiculo, Optional.of(vaga));
                
            }
            }
            catch(Exception e){
                System.out.println("Excecao bem doida");
            }
            finally{
                portaoEntrada.release();
            }
        return Optional.ofNullable(vaga);
    }

    /**
     * Prioritário: tenta vaga prioritária sem esperar (tryAcquire()); se não houver, cai para regular.
     * Normal: apenas vaga regular.
     * A espera por vaga regular usa tryAcquire(TIMEOUT_VAGA_MS, TimeUnit.MILLISECONDS).
     */
    private Optional<TipoVaga> alocarVaga(Veiculo veiculo) throws InterruptedException {
        TipoVaga vaga = null;
        if(ehPrioritario(veiculo)){
            vaga = TipoVaga.PRIORITARIA;
            if(!vagasPrioritarias.tryAcquire()){
                vagasRegulares.tryAcquire(TIMEOUT_VAGA_MS, TimeUnit.MILLISECONDS);
            }
        }
        else{
            vaga = TipoVaga.REGULAR;
            vagasRegulares.tryAcquire(TIMEOUT_VAGA_MS, TimeUnit.MILLISECONDS);
        }
        return Optional.ofNullable(vaga);
    }

    private void registrarResultadoEntrada(Veiculo veiculo, Optional<TipoVaga> vaga) {
        // TODO 4:
        // - Se há vaga: incrementar totalEntradas e logar "conseguiu vaga <TIPO>"
        // - Senão: incrementar totalDesistencias e logar "DESISTIU: sem vaga após 2000ms"
        
    }

    private boolean ehPrioritario(Veiculo veiculo) {
        return veiculo.getTipo() == TipoVeiculo.PRIORITARIO;
    }

    // ==================== SAÍDA ====================

    public void sair(Veiculo veiculo, TipoVaga vaga) throws InterruptedException {
        // TODO 6:
        // 1. Adquirir o portão de saída (FORA do try)
        // 2. Dentro do try:
        //    - Log: "está saindo do estacionamento..."
        //    - Liberar a vaga correta (use semaforoDa(vaga))
        //    - Log: "saiu com sucesso!"
        // 3. No finally: liberar o portão de saída
    }

    /** Devolve o semáforo correspondente ao tipo de vaga. */
    private Semaphore semaforoDa(TipoVaga vaga) {
        // TODO 7: uma linha
        return null; // placeholder
    }

    // ==================== CONSULTAS ====================

    public int getTotalEntradas() {
        return totalEntradas.get();
    }

    public int getTotalDesistencias() {
        return totalDesistencias.get();
    }

    public void exibirStatus() {
        System.out.println("\n=== STATUS DO ESTACIONAMENTO ===");
        // TODO 8: exibir, usando availablePermits():
        //   Vagas regulares disponíveis: x/5
        //   Vagas prioritárias disponíveis: y/2
        //   Total de entradas / Total de desistências
        System.out.println("================================");
    }
}
