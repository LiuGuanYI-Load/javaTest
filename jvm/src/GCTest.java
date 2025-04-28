import java.lang.management.*;
import java.util.List;

public class GCTest {
    public static void main(String[] args) {
        List<GarbageCollectorMXBean> gcBeans = ManagementFactory.getGarbageCollectorMXBeans();
        for (GarbageCollectorMXBean gcBean : gcBeans) {
            System.out.println("GC Name: " + gcBean.getName());
        }
        new Thread(()->{
            System.out.println("-------------------------------------");
            for (GarbageCollectorMXBean gcBean : gcBeans) {
                System.out.println("GC Name: " + gcBean.getName());
            }
        }).start();

    }
}