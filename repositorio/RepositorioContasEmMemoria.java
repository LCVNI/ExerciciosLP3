package repositorio;
import modelo.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import excecao.ContaNaoEncontradaException;
public class RepositorioContasEmMemoria implements RepositorioContas {
    private Map<Integer, Conta> contas = Collections.synchronizedMap(new HashMap<>());
    @Override
    public void salvar(Conta conta) {
        Integer key = conta.getId();
        if(contas.putIfAbsent(key, conta) != null){
            System.out.println("Conta ja existente");
        }
    }

    @Override
    public Conta buscar(int id) {
       Conta conta = contas.get(id);
       if(conta == null){
        throw new ContaNaoEncontradaException("Conta nao encontrada");
       }
        return conta;
    }

    @Override
    public int quantidade() {
        return contas.size();
    }

    @Override
    public long saldoTotal() {
        long total = 0;
        for(Conta c: contas.values()){
            total += c.getSaldo();
        }
        return total;
    }

    @Override
    public boolean todasConsistentes() {
        boolean resultado = true;
       for(Conta c: contas.values()){
            if(!c.saldoConsistente()){resultado = false;}
       }
        return resultado;
    }

}

