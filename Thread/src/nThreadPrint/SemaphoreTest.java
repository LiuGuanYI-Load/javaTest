package nThreadPrint;

import java.util.concurrent.Semaphore;

public class SemaphoreTest {
    Semaphore semaphore = new Semaphore(5); // 最多 3 个线程同时访问

    public void accessResource() throws InterruptedException {
        try {
            semaphore.acquire(1); // 获取许可证
            System.out.println(Thread.currentThread().getName() + " 访问资源");
            Thread.sleep(1000); // 模拟工作
        } finally {
            System.out.println(Thread.currentThread().getName() + "yi man li");
            semaphore.release(1); // 释放许可证
        }
    }

    public static void main(String[] args) {
        SemaphoreTest example = new SemaphoreTest();
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                try {
                    example.accessResource();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }, "线程-one" + i).start();
            new Thread(() -> {
                try {
                    example.accessResource();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }, "线程-two" + i).start();
        }
    }
}
