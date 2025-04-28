import java.lang.ref.WeakReference;

public class WeakRefDemo {
    static class BigObject {
        byte[] data = new byte[1024 * 1024]; // 1MB
    }

    public static void main(String[] args) throws InterruptedException {
        // 创建弱引用
        BigObject obj = new BigObject();
        WeakReference<BigObject> weakRef = new WeakReference<>(obj);
        System.out.println("弱引用创建，get: " + weakRef.get());

        // 断开强引用
        obj = null;
        System.out.println("强引用清空，get: " + weakRef.get());

        // 手动触发GC
        System.out.println("触发GC...");
        System.gc();
        Thread.sleep(1000); // 给GC点时间

        // 检查结果
        System.out.println("GC后，get: " + weakRef.get());

        // 再跑一会儿，观察自然GC
        System.out.println("等待自然GC...");
        Thread.sleep(5000);
        System.out.println("5秒后，get: " + weakRef.get());
    }
}