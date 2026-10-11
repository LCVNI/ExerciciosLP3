package excecao;

public class ContaNaoEncontradaException extends RuntimeException{
    private static final long serialVersionUID = 1L;
    public ContaNaoEncontradaException(String mensagem){
        super(mensagem);
    }
}
