import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class GCDemo {
    // 强引用对象
    static class StrongObject {
        byte[] data = new byte[1024 * 1024]; // 占1MB内存
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("开始实验...");

        // 1. 强引用
        List<StrongObject> strongList = new ArrayList<>();
        strongList.add(new StrongObject());
        System.out.println("强引用对象创建，1MB占用");

        // 2. 软引用
        SoftReference<StrongObject> softRef = new SoftReference<>(new StrongObject());
        System.out.println("软引用对象创建，1MB占用");

        // 3. 弱引用
        WeakReference<StrongObject> weakRef = new WeakReference<>(new StrongObject());
        System.out.println("弱引用对象创建，1MB占用");

        // 4. 虚引用（需要ReferenceQueue配合）
        ReferenceQueue<StrongObject> queue = new ReferenceQueue<>();
        PhantomReference<StrongObject> phantomRef = new PhantomReference<>(new StrongObject(), queue);
        System.out.println("虚引用对象创建，1MB占用");

        // 打印初始状态
        System.out.println("初始状态：");
        System.out.println("软引用get: " + softRef.get());
        System.out.println("弱引用get: " + weakRef.get());
        System.out.println("虚引用get: " + phantomRef.get()); // 总是null
        System.out.println("虚引用队列: " + queue.poll());

        // 让程序跑一会儿，方便VisualVM监控
        Thread.sleep(5000);

        // 手动触发GC
        System.out.println("手动GC...");
        System.gc();
        Thread.sleep(1000);

        // 检查引用状态
        System.out.println("GC后状态：");
        System.out.println("软引用get: " + softRef.get()); // 内存够可能还活着
        System.out.println("弱引用get: " + weakRef.get()); // 通常null
        System.out.println("虚引用队列: " + queue.poll()); // 被回收后入队

        // 清空强引用，模拟内存压力
        System.out.println("清空强引用，制造内存压力...");
        strongList = null;
        Thread.sleep(1000);
        System.gc();
        Thread.sleep(1000);

        // 再次检查
        System.out.println("第二次GC后状态：");
        System.out.println("软引用get: " + softRef.get()); // 内存紧张可能被回收

        // 制造更大内存压力
        System.out.println("制造大内存压力...");
        List<byte[]> pressure = new ArrayList<>();
        try {
            while (true) {
                pressure.add(new byte[1024 * 1024 * 10]); // 每次10MB
            }
        } catch (OutOfMemoryError e) {
            System.out.println("OOM触发，检查软引用...");
            System.out.println("软引用get: " + softRef.get()); // 应该null
        }

        System.out.println("实验结束，保持运行供VisualVM观察...");
        Thread.sleep(600); // 跑1分钟
    }
}