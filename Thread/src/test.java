import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class test {
    public static void main(String[] args) {
        ThreadPoolExecutor pool = new ThreadPoolExecutor(2,4,100, TimeUnit.MICROSECONDS,new SynchronousQueue<>(),new ThreadPoolExecutor.AbortPolicy());
        pool.execute(()->{
            System.out.println(6+Thread.currentThread().getName());
        });
    }
}