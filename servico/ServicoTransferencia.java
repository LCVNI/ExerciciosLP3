package servico;
import repositorio.*;
import excecao.SaldoInsuficienteException;
import metricas.*;
import tarifa.*;
import modelo.*;

public class ServicoTransferencia {
    private RepositorioContas repositorio;
    private MetricasOperacao metricas;
    private long tarifa;
    public ServicoTransferencia(RepositorioContas repositorio, MetricasOperacao metricas, long tarifa){
        this.repositorio = repositorio;
        this.metricas = metricas;
        this.tarifa = tarifa;
    }
    public boolean executar(Transferencia t){
        Conta contaOrigem = repositorio.buscar(t.origem());
        long debitar = t.valor() + this.tarifa;
        try {
            contaOrigem.debitar(debitar);
        } catch (SaldoInsuficienteException e) {
            metricas.rgistarRecusada();
            return false;
        }
        metricas.registrarAprovada();
        return true;
    }
}
