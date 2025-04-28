import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class SimpleAlternativePrint2 {
    private static int count = 1;
    private static final ReentrantLock lock = new ReentrantLock();
    private static final Condition oddCondition = lock.newCondition();
    private static final Condition evenCondition = lock.newCondition();

    public static void main(String[] args) {
        new Thread(() -> printOdd(), "OddThread").start();
        new Thread(() -> printEven(), "EvenThread").start();
    }

    static void printOdd() {
        while (true) {
            lock.lock();
            try {
                if (count > 100000) {
                    evenCondition.signal();
                    break;
                }
                if (count % 2 == 1) {
                    System.out.println("OddThread: " + count++);
                    evenCondition.signal();
                } else {
                    oddCondition.await();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                lock.unlock();
            }
        }
    }

    static void printEven() {
        while (true) {
            lock.lock();
            try {
                if (count > 100000) {
                    oddCondition.signal();
                    break;
                }
                if (count % 2 == 0) {
                    System.out.println("EvenThread: " + count++);
                    oddCondition.signal();
                } else {
                    evenCondition.await();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                lock.unlock();
            }
        }
    }
}