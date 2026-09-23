package modelo;

public record Leitura (int sensorId, TipoSensor tipo, double valor) {
    @Override
    public String toString(){
        return sensorId + " - " + tipo + " - " + String.format("%.2f", valor) + tipo.getUnidade();
    }
}
