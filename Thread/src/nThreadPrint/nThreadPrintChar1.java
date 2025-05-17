package nThreadPrint;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class nThreadPrintChar1 {
    private static volatile int expectedThreadId = 0;
    private static volatile  char curChar = 'A';
    private static int totalThread ;
    public static void main(String[] args) {
        print2(5);
    }
    static Object lock = new Object();
    public static void print(int n){
        totalThread = n;
        for (int i = 0; i < n; i++) {
            final int curThreadId = i;
            new Thread(()->{
                synchronized(lock){
                    while(curChar <= 'z'){
                        if(curThreadId == expectedThreadId){
                            System.out.println(Thread.currentThread().getName());
                            System.out.println(curChar);
                            curChar++;
                            expectedThreadId = (expectedThreadId + 1) % n;
                            lock.notifyAll();
                        }else{
                            try {
                                lock.wait();
                            }catch (InterruptedException e){
                                System.out.println("interrupted exp");
                            }
                        }
                    }
                }
            },"Thread " + (i + 1) + "execute print task" ).start();
        }
        //notify all threads
        lock.notifyAll();
    }



    private static final ReentrantLock lock1 = new ReentrantLock();
    private static Condition[] conditions;
    private static volatile int expectedThreadId1 = 0;
    private static volatile int totalThread1;
    private static volatile char curChar1 = 'A';



    public static void print2(int n) {
        totalThread = n;
        conditions = new Condition[n];
        for (int i = 0; i < n; i++) {
            conditions[i] = lock1.newCondition();
        }

        for (int i = 0; i < n; i++) {
            final int curThreadId = i;
            new Thread(() -> {
                lock1.lock();
                try {
                    while (curChar1 <= 'Z') {
                        if (curThreadId == expectedThreadId1) {
                            System.out.println(Thread.currentThread().getName() + ": " + curChar1);
                            curChar1++;
                            expectedThreadId1 = (expectedThreadId1 + 1) % n;
                            conditions[expectedThreadId1].signal();
                        } else {
                            conditions[curThreadId].await();
                        }
                    }
                    for (Condition condition : conditions) {
                        condition.signal();
                    }
                } catch (InterruptedException e) {
                    System.out.println("Interrupted: " + e.getMessage());
                } finally {
                    lock1.unlock();
                }
            }, "T" + (i + 1)).start();
        }
    }
}
