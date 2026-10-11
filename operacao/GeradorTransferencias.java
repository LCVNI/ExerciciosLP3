package operacao;
import modelo.Transferencia;
public class GeradorTransferencias {
    private int origem = (int) (Math.random() * 5);
    private int destino = aleatorio();
    private long max;
    private long valor = 1 + (long) (Math.random()*max);

    public GeradorTransferencias(long maximo){
        this.max = maximo;
    }

    Transferencia t = new Transferencia(origem, destino, valor);
    
    public int aleatorio(){
        destino = (int) (Math.random() * 5);
        while(destino == origem){
            destino = (int) (Math.random() * 5);
        }
        return destino;
    }
}
