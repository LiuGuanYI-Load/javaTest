package nThreadPrint;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ThreeThreadDemo {
    private static int count = 0;
    private static final int MAX = 100;
    private static Lock lock = new ReentrantLock();
    private static Condition[] conditions = new Condition[3];

    static {
        for (int i = 0; i < 3; i++) {
            conditions[i] = lock.newCondition();
        }
    }

    public static void main(String[] args) {
        Thread[] threads = new Thread[3];
        for (int i = 0; i < 3; i++) {

            final int threadId = i;
            threads[i] = new Thread(() -> {
                while (true) {
                    lock.lock();
                    try {
                        // 如果不是自己的轮次，就等着
                        while (count % 3 != threadId && count <= MAX) {
                            conditions[threadId].await();
                        }
                        if (count > MAX) {
                            break;
                        }
                        System.out.println("线程" + (threadId + 1) + ": " + count);
                        count++;
                        // 唤醒下一个线程
                        conditions[(threadId + 1) % 3].signal();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        lock.unlock();
                    }
                }
            });
            threads[i].start();
        }
    }
}