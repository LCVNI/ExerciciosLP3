package agendamento;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import dados.RegistroEventos;
public class BackupEventos implements Runnable {
    RegistroEventos eventos;
    public BackupEventos(RegistroEventos eventos){
        this.eventos = eventos;
    }
    List<String> backup = new ArrayList<>();
    @Override
    public void run() {
        backup = eventos.copiar();

        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); 
            return; 
        }
    }
}
