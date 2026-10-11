package modelo;

public class ContaConcorrente extends Conta{
    private long chequeEspecial;
    public ContaConcorrente(long saldo, int id, long chequeEspecial){
        super(id, saldo);
        this.chequeEspecial = chequeEspecial;
    }
    public long getChequeEspecial() {
        return chequeEspecial;
    }
    @Override 
    public long limite(){
        return chequeEspecial;
    }
}
