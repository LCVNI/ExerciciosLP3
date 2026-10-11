package modelo;
import java.util.concurrent.atomic.AtomicLong;

import excecao.SaldoInsuficienteException;;
public abstract class Conta {
    private int id;
    private AtomicLong saldo;
    public Conta(int id, long saldo){
        this.id = id;
        this.saldo.set(saldo);
    }
    public long getSaldo(){return saldo.get();}
    public int getId(){return id;}
    public void debitar(long valor) throws SaldoInsuficienteException{
        if(saldo.get() - valor < -limite()){
            throw new SaldoInsuficienteException("Saldo insuficiente\n ID: " + id + "\n Valor: " + valor , id);
        }
        while(!saldo.compareAndSet(saldo.get(), saldo.get() - valor)){}
    }

    public void creditar (long valor){
        saldo.addAndGet(valor);
    }
    public boolean saldoConsistente(){return saldo.get() >= -limite(); }

    protected abstract long limite();
}
