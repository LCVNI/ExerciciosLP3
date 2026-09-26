package dados;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
public class RegistroEventos {
    List<String> eventos = Collections.synchronizedList(new ArrayList<>());

    public void registrar(String evento){
        eventos.add(evento);
    }

    public List<String> copiar(){
        synchronized(eventos){
            return new ArrayList<>(eventos);
        }
    }
}
