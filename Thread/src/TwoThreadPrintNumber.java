import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class TwoThreadPrintNumber {
    private static final ReentrantLock lock = new ReentrantLock();
    private static final Condition n1 = lock.newCondition();
    private static final Condition n2 = lock.newCondition();
    private static AtomicInteger cur = new AtomicInteger(0);
    private static int curNum = 0;
    public static void main(String[] args) {
        new Thread(()->{
            for (int i = 0;i < 50; i ++){
                printn1();;
            }
        }).start();
        new Thread(()->{
            for (int i = 0;i < 50; i ++){
                printn2();;
            }
        }).start();
    }
    static void print1(){
        lock.lock();
        try{
            while(cur.get() % 2 !=0){
                n1.await();
            }
            cur.getAndIncrement();
            System.out.println(cur.get());
            n2.signal();
        }catch(InterruptedException e){
            System.out.println("interrupt");
        }
        finally {
            lock.unlock();
        }
    }
    static void print2(){
        lock.lock();
        try{
            while(cur.get() % 2 ==0){
                n2.await();
            }
            cur.getAndIncrement();
            System.out.println(cur.get());
            n1.signal();
        }catch(InterruptedException e){
            System.out.println("interrupt");
        }
        finally {
            lock.unlock();
        }
    }

    static void printn1(){
        lock.lock();
        try{
            while(curNum % 2 !=0){
                n1.await();
            }
            curNum++;
            System.out.println(curNum);
            n2.signal();
        }catch(InterruptedException e){
            System.out.println("interrupt");
        }
        finally {
            lock.unlock();
        }
    }
    static void printn2(){
        lock.lock();
        try{
            while(curNum % 2 ==0){
                n2.await();
            }
            curNum++;
            System.out.println(curNum);
            n1.signal();
        }catch(InterruptedException e){
            System.out.println("interrupt");
        }
        finally {
            lock.unlock();
        }
    }
}
