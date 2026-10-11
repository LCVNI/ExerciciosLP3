package modelo;

public record Transferencia(int origem, int destino, long valor){
    public Transferencia{
        if(valor <= 0 || origem == destino){
            throw new IllegalArgumentException("Valor negativo ou origem igual a destino");
        }
    }
}
