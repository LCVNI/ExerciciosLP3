import java.util.concurrent.TimeUnit;

public class Painel implements Runnable {
    @Override 
    public void run(){
       
        while(!Main.sinalParada){
            try {
                Integer valor = Leitura.leituras.poll(50, TimeUnit.MILLISECONDS);
                if(valor == null){
                    break;
                }
                System.out.println("Thread-4 retirou " + valor);
            } catch (InterruptedException e) {
                e.printStackTrace();
                }
        }

        while(true){
            System.out.println("Recebi sinal de parada!");
            Integer valor = Leitura.leituras.poll();
            if(valor == null){
                break;
            }
            System.out.println("Thread-4 retirou " + valor);
            }
        }
}
