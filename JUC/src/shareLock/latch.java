package shareLock;

import java.util.concurrent.CountDownLatch;

public class latch {
    public static void main(String[] args) {
        CountDownLatch latch = new CountDownLatch(2);
        new Thread(() -> {
            latch.countDown();
            System.out.println("Thread1 execute...");
        }).start();


        new Thread(() -> {
            try {
                Thread.sleep(1000);
                latch.countDown();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Thread2 execute...");
        }).start();


        new Thread(() -> {
            System.out.println("Thread3 execute");
            latch.countDown();
        }).start();
        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


        System.out.println("之后的执行");
    }
}
