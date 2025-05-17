package nThreadPrint;//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.locks.Condition;
//import java.util.concurrent.locks.ReentrantLock;
//
//public class nThreadPrint.ProducerAndComsumer {
//    private static  final ReentrantLock lock = new ReentrantLock();
//    private  static final Condition noEmpty = lock.newCondition();
//    private  static final Condition noFull = lock.newCondition();
//    private static final int BufferSize = 6;
//    private static List<Integer> buffer = new ArrayList<>(BufferSize);
//    public static void main(String[] args) {
//        for(int i = 0; i < 10; i++){
//            new Thread(()->{
//                produce();
//            },"producer").start();
//
//        }
//        for(int i = 0; i < 8; i++){
//            new Thread(()->{
//                consumer();
//            },"consumer").start();
//
//        }
//
//    }
//
//
//    static void produce() {
//        lock.lock();
//        try{
//            while(buffer.size() == BufferSize){
//                noFull.await();
//                System.out.println("produce wait");
//            }
//            buffer.add(1);
//            System.out.println("produce ************");
//            noEmpty.signal();
//        }catch (InterruptedException e) {
//            System.out.println("Interrupted Exception");
//        }finally{
//            lock.unlock();
//        }
//    }
//    static void consumer(){
//        lock.lock();
//        try{
//            while(buffer.size() == 0){
//                noEmpty.await();
//            }
//            buffer.remove(buffer.size() - 1);
//            System.out.println("consume **************");
//            noFull.signal();
//        }catch (InterruptedException e) {
//            System.out.println("Interrupted Exception");
//        }finally{
//            lock.unlock();
//        }
//    }
//}
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerAndComsumer {
    private static final ReentrantLock lock = new ReentrantLock();
    private static final Condition noEmpty = lock.newCondition();
    private static final Condition noFull = lock.newCondition();
    private static final int BUFFER_SIZE = 6;
    private static final List<Integer> buffer = new ArrayList<>(BUFFER_SIZE);

    // 用于同步日志输出的锁
    private static final ReentrantLock logLock = new ReentrantLock();

    public static void main(String[] args) {
        // 启动10个生产者线程
        for (int i = 0; i < 10; i++) {
            new Thread(() -> produce(), "Producer-" + i).start();
        }
        // 启动8个消费者线程
        for (int i = 0; i < 8; i++) {
            new Thread(() -> consumer(), "Consumer-" + i).start();
        }
    }

    static void produce() {
        while (true) { // 让生产者持续运行，便于观察
            lock.lock();
            try {
                while (buffer.size() == BUFFER_SIZE) {
                    log("[" + Thread.currentThread().getName() + "] Buffer full, waiting...");
                    noFull.await();
                }
                buffer.add(1);
                log("[" + Thread.currentThread().getName() + "] Produced, buffer size: " + buffer.size());
                noEmpty.signal();
            } catch (InterruptedException e) {
                log("[" + Thread.currentThread().getName() + "] Interrupted Exception");
            } finally {
                lock.unlock();
            }
            // 模拟生产间隔
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                log("[" + Thread.currentThread().getName() + "] Sleep interrupted");
            }
        }
    }

    static void consumer() {
        while (true) { // 让消费者持续运行，便于观察
            lock.lock();
            try {
                while (buffer.size() == 0) {
                    log("[" + Thread.currentThread().getName() + "] Buffer empty, waiting...");
                    noEmpty.await();
                }
                buffer.remove(buffer.size() - 1);
                log("[" + Thread.currentThread().getName() + "] Consumed, buffer size: " + buffer.size());
                noFull.signal();
            } catch (InterruptedException e) {
                log("[" + Thread.currentThread().getName() + "] Interrupted Exception");
            } finally {
                lock.unlock();
            }
            // 模拟消费间隔
            try {
                Thread.sleep(150);
            } catch (InterruptedException e) {
                log("[" + Thread.currentThread().getName() + "] Sleep interrupted");
            }
        }
    }

    // 线程安全的日志方法
    private static void log(String message) {
        logLock.lock();
        try {
            System.out.println(System.currentTimeMillis() + " " + message);
        } finally {
            logLock.unlock();
        }
    }
}