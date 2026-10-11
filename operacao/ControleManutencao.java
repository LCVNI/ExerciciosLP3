package operacao;

public class ControleManutencao {
    private volatile boolean flag;
    public void iniciar(){
        flag = false;
    }
    public void finalizar(){
        flag = true;
    }
    public boolean estaAtiva(){
        return flag;
    }
    public void aguardarLiberacao(){
        while(estaAtiva()){
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
