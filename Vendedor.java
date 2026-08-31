public class Vendedor implements Runnable {
    String nome;
    public Vendedor(String nome){
        this.nome = nome;
    }
    @Override
    public void run(){
        while(true){
            synchronized(Vendedor.class){
            int qtd = Bilheteria.getIngresso();
            if(qtd == 0){
                System.out.println("\nAcabaram os ingressos");
                return;
            }
            qtd -= 1;
            Bilheteria.setIngresso(qtd);
            System.out.println("\nRestam " + qtd + " ingressos");
            System.out.println("\nNome: "+ this.nome);
            }
        try {
            Thread.sleep(100); 
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        }
    }
}
