package controle;

public class EstadoSistema {
   public static volatile boolean flag = true;
   public boolean estaAtivo(){
    return flag;
   }
   public void desligar(){
    flag = false;
   }
}
