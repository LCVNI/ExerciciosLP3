package modelo;
public class ContaPoupanca extends Conta{
    public ContaPoupanca(int id, long saldo){
        super(id, saldo);
    }
    @Override
    public long limite(){return 0;}
}