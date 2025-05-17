package nThreadPrint;

import java.util.concurrent.locks.ReentrantLock;

public class SimpleAlternativePrint {
    private static volatile  int  count = 1;
    private static final ReentrantLock lock=new ReentrantLock();
    private static ThreadLocal<String> local=new ThreadLocal<>();
    public static void main(String[] args) {
        new Thread(() -> printOdd(), "OddThread").start();
        new Thread(() -> printEven(), "EvenThread").start();
    }

    static void printOdd() {

        while (count <= 100000) {

            if (count % 2 == 1) {
                System.out.println("OddThread: " + count++);

            }
        }
    }

    static void printEven() {
        while (count <= 100000) {

                if (count % 2 == 0) {
                    System.out.println("EvenThread: " + count++);

                }
            }

    }
}