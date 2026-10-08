package app;
import sensor.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("[Main] Inicializando os componentes do sistema...");

        // 1. Criamos a lista que vai conter todos os sensores
        List<Sensor> listaSensores = new ArrayList<>();

        // 2. Instanciamos os 2 sensores de temperatura (IDs 1 e 2)
        // Eles usam o polimorfismo, pois são do tipo 'Sensor'
        listaSensores.add(new SensorTemperatura(1));
        listaSensores.add(new SensorTemperatura(2));

        // 3. Instanciamos os 2 sensores de umidade (IDs 3 e 4)
        listaSensores.add(new SensorUmidade(3));
        listaSensores.add(new SensorUmidade(4));

        // Configurações especificadas no enunciado:
        int maxLeituras = 8;
        int maxAlertas = 5;

        // 4. Criamos a Central de Monitoramento passando a lista e as configurações
        CentralMonitoramento central = new CentralMonitoramento(listaSensores, maxLeituras, maxAlertas);

        System.out.println("[Main] Disparando o monitoramento central...");
        
        // 5. Iniciamos a simulação. Esta linha vai segurar a Main até tudo terminar.
        central.iniciarMonitoramento();

        System.out.println("[Main] Programa principal finalizado com sucesso.");
    }
}
