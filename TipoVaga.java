/**
 * Tipo de vaga efetivamente ocupada por um veículo.
 * Permite que a saída libere o semáforo correto sem depender de flags booleanas.
 */
public enum TipoVaga {
    REGULAR("REGULAR"),
    PRIORITARIA("PRIORITÁRIA");

    private final String rotulo;

    TipoVaga(String rotulo) {
        this.rotulo = rotulo;
    }

    @Override
    public String toString() {
        return rotulo;
    }
}
