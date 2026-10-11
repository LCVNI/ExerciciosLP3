package repositorio;
import modelo.*;
public interface RepositorioContas {
    void salvar(Conta conta);
    Conta buscar(int id);
    int quantidade();
    long saldoTotal();
    boolean todasConsistentes();
}
