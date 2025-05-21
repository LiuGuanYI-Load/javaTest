package shareLock;

import java.util.concurrent.Semaphore;

public class semaphore {
    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(2);
        Runnable task = () -> {
            try {
                semaphore.acquire();
                System.out.println(Thread.currentThread().getName() + " 获取许可");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                semaphore.release();
                System.out.println(Thread.currentThread().getName() + " 释放许可");
            }
        };
        for (int i = 0; i < 4; i++) {
            new Thread(task).start();
        }

    }
}
