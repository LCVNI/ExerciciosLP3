public class Main {
    public static void main(String[] args) {
        Catracas runnable = new Catracas();
        Thread t0 = new Thread(runnable);
        Thread t1 = new Thread(runnable);
        Thread t2 = new Thread(runnable);
        Thread t3 = new Thread(runnable);

        t0.start();
        t1.start();
        t2.start();
        t3.start();
        try{
            Thread.sleep(10000);
        } catch(InterruptedException e){
            System.out.println("Sleep interrompido");
        }

        System.out.println("Log 1: " + Catracas.historicoLogs.get(0));
        System.out.println("Ultimo log: " + Catracas.historicoLogs.get(Catracas.historicoLogs.size() - 1));

    }
}
