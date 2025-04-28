public class DeadLockDemo {
    static String a="lock1";
    static String b="lock2";
    public static void main(String[] args) {
        new Thread(()->{
            synchronized (a){
                System.out.println("t2 get the lock a");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                synchronized (b){
                    System.out.println("b");
                }
            }




        }).start();
        new Thread(()->{
            synchronized (b){
                System.out.println("t2 get the lock b");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                synchronized (a){
                    System.out.println("a");
                }
            }



        }).start();
    }
}
