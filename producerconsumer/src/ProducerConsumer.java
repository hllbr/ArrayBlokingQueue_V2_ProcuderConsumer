
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ProducerConsumer {
    
    Random random = new Random();
    
    Object lock = new Object();
    
    //Linkedlist queue interface implemente ettiğini bildiğimiz için bu sefer linkedlist kullanıcam
    
    Queue<Integer> kuyruk = new LinkedList<Integer>();
    
    private int Limit = 10;
    
    //LinkedListin boyutu 10'u geçmediği sürece işlemlere devam edeceğiz
    public void Consumer(){
        while (true) {            
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                System.out.println("Bekletme hatası");
            }
            synchronized(lock){//anahtar üzerinden senkron blok oluşturuyorum
                //kim lock'u önce kaparsa bu blok ona ait olacak.
                //boyut 10 ise producer bekletmemiz gerekiyor.
                if(kuyruk.size() == Limit){
                    try {
                    //koşul sağlandığında producer bekletilmeli.başka bir consumerın bu produceri uyandırması gerekiyor (tanımlamaya çalıştığım ifadenin açılımı)
                    lock.wait();
                    } catch (InterruptedException ex) {
                      System.out.println("wait metodunda bir sorunla karşılaşıldı.");
                    }
                    
                }
                Integer val = random.nextInt(1000);
                kuyruk.offer(val);
                System.out.println("Producer Üretiyor : ");
                try {
                    Thread.sleep(150);
                   
                } catch (InterruptedException ex) {
                    System.out.println("Kesme sorunu");
                }
               System.out.println(val+" eklendi");
               lock.notify();//bizim consumerımız kuyruk boş olduğunda bekleyecek.Yani wait işlemi gerçekleştirecek ve procuderimizda aynı şekilde kuyruk boyutu 10 olduğunda bekleyecek.bunların birbirlerini sürekli olarak uyandırmaları gerekiyor.
               //eğer bizim bekleyen bir consumer threadimiz varsa bizim buradaki synchronized blokumuzda bu procuderın bunu uyandırması gerektiği için bu işlem gerçekleştirildi
               //güvenlik açısından gerçekleştirdik.Burada uyuyan bir consumer varsa direkt olarak uyanmış olacak.//eğer uyuan bir consumer varsa uyandırmış olacağız.
            }
        }
        
    }
    public void Producer(){
        Scanner scn = new Scanner(System.in);
        while(true){
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                System.out.println("Uyku modu sırasında hata oluştu");
            }
            synchronized(lock){
                /*
                burada ilk olarak kuyruk boyutunu kontrol ediyoruz.
                producer henüz herhangi bir değer koymadıysa bir bekletmek istiyoruz yapımızı
                */
                if(kuyruk.size() == 0){
                    try {
                        lock.wait();
                        //buradaki yapımızın başka bir producer tarafından uyandırılması gerekiyor
                    } catch (InterruptedException ex) {
                        System.out.println("Bekleme sırasında hata oluştu");
                    }
                    
                }
                Integer val =  kuyruk.poll();//kuyruktaki ilk elemanı al
                System.out.println("Consumer Tüketiyor...");
                try {
                    Thread.sleep(1200);
                } catch (InterruptedException ex) {
                    System.out.println("Tüketim sırasında hata meydana geldi");
                }
                System.out.println("tüketildi : "+val);
                System.out.println("Kuyruk Boyutu(Anlık) : "+kuyruk.size());
                //burdan sonra bekleyen bir producer varsa bu yapı içerisinde (consumerda) uyandırmamız gerekiyor.
                lock.notify();//beklemekte olan bir producer varsa uyandır.
            }
        }
        
    }
}
/*
Kuyrukta FIFO yapısı var ilk giren ilk çıkar
*/