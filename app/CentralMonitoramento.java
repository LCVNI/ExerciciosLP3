package app;
import dados.*;
import controle.*;
import coordenacao.ConsolidadorRodada;
import coordenacao.TarefaSensor;
import modelo.*;
import sensor.Sensor;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import agendamento.Agendador;
import agendamento.BackupEventos;
import agendamento.PainelStatus;

import java.util.ArrayList;
import java.util.List;

public class CentralMonitoramento {
    private int maxLeituras;
    private int maxAlertas;
    private List<Sensor> sensores;

    //dados e controle
    private final Estatisticas stats;
    private final RegistroEventos eventos;
    private final UltimasLeituras leituras;
    private final EstadoSistema estado;
    private final PoliticaDesligamento politica;

    public CentralMonitoramento(List<Sensor> sensores, int maxLeituras, int maxAlertas){
        this.sensores = sensores;
        this.maxAlertas = maxAlertas;
        this.maxLeituras = maxLeituras;
        this.stats = new Estatisticas();
        this.eventos = new RegistroEventos();
        this.leituras = new UltimasLeituras();
        this.estado = new EstadoSistema();
        this.politica = new PoliticaDesligamento(maxAlertas);
        
    }
    public void iniciarMonitoramento() {
        System.out.println("Iniciando a Central de Monitoramento...");

        // 1. Criação da CyclicBarrier: o número de partes é o total de sensores.
        // A ação executada automaticamente quando a barreira enche é o ConsolidadorRodada.
        CyclicBarrier barreira = new CyclicBarrier(
            sensores.size(), 
            new ConsolidadorRodada(stats, leituras, estado)
        );

        // 2. Abertura do Agendador e do Pool Fixo dentro do try-with-resources
        try (Agendador agendador = new Agendador();
             ExecutorService poolFixo = Executors.newFixedThreadPool(sensores.size())) {

            // 3. Agendamento das tarefas de segundo plano usando os métodos do seu Agendador
            // Painel a cada 500ms (taxa fixa)
            agendador.aTaxaFixa(new PainelStatus(stats, estado), 0, 500);
            
            // Backup a cada 1s (atraso fixo, iniciando após 1s)
            agendador.comAtrasoFixo(new BackupEventos(eventos), 1000, 1000);

            // 4. Preparação da lista de Callables (TarefaSensor) para o invokeAll
            List<TarefaSensor> tarefas = new ArrayList<>();
            for (Sensor sensor : sensores) {
                // Passamos para cada tarefa o sensor individual e TODOS os objetos compartilhados
                tarefas.add(new TarefaSensor(barreira, sensor, this, estado, stats, leituras, eventos));
            }

            System.out.println("Disparando threads dos sensores...");
            
            // 5. O invokeAll dispara todas as tarefas e BLOQUEIA esta linha até que 
            // todas as threads dos sensores terminem de rodar.
            List<Future<Integer>> resultados = poolFixo.invokeAll(tarefas);

            // 6. Soma dos resultados das leituras
            int totalLeiturasComputadas = 0;
            for (Future<Integer> resultado : resultados) {
                // O .get() pega o Integer retornado por cada TarefaSensor
                totalLeiturasComputadas += resultado.get();
            }

            // 7. Impressão do relatório final após o encerramento de tudo
            System.out.println("\n========================================");
            System.out.println("      RELATÓRIO FINAL DO MONITORAMENTO  ");
            System.out.println("========================================");
            System.out.println("Total de leituras reais feitas: " + totalLeiturasComputadas);
            System.out.println("Total de rodadas concluídas: " + stats.getRodadas());
            System.out.println("Total de alertas emitidos: " + stats.getAlertas());
            System.out.println("========================================");

        } catch (Exception e) {
            System.err.println("Erro durante a simulação do monitoramento.");
            e.printStackTrace();
        }
        
        // Ao sair do bloco try, o agendador invoca o close() automaticamente, 
        // cancelando o PainelStatus e o BackupEventos sem deixar threads órfãs.
        System.out.println("Central de monitoramento desligada com segurança.");
    }
    public int getMaxLeituras(){return maxLeituras;}
    public int gerMaxAlertas(){return maxAlertas;}
}
