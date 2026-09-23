package modelo;

public enum TipoSensor {
    TEMPERATURA("C"), UMIDADE("%");
    private final String unidade;

    TipoSensor(String unidade){
        this.unidade = unidade;
    }

    public String getUnidade(){return unidade;}
}

