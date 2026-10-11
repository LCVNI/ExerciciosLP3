package excecao;

public class SaldoInsuficienteException extends Exception {
    private final int idConta;
    public SaldoInsuficienteException(String mensagem, int idConta){
        super(mensagem);
        this.idConta = idConta;
    }
    public int getIdConta(){
        return idConta;
    }
}
