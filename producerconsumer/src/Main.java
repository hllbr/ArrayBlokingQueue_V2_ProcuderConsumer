
import java.util.logging.Level;
import java.util.logging.Logger;


public class Main {
    public static void main(String[] args) {
        /*
        wait ve notify ile yapıyı tekrar dizayn etmeye çalışıyorum
        */
        ProducerConsumer pc = new ProducerConsumer();
        Thread pro = new Thread(new Runnable() {
            @Override
            public void run() {
                pc.Producer();
            }
        });
        Thread con = new Thread(new Runnable() {
            @Override
            public void run() {
                pc.Consumer();
            }
        });
        pro.start();
        con.start();
        
        try {
            pro.join();
            con.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
