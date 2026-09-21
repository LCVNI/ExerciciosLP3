public class Nota {
    int id;
    String cliente;
    double valor;
    String uf;
    public Nota(int id, String cliente, double valor, String uf){
        this.uf = uf;
        this.cliente = cliente;
        this.id = id;
        this.valor = valor;
    }
}
