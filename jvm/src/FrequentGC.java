import java.util.ArrayList;
import java.util.List;

public class FrequentGC {
    public static void main(String[] args) throws InterruptedException {
        // 控制 List 大小，模拟部分对象存活，部分被回收
        List<byte[]> memoryHog = new ArrayList<>();
        int maxSize = 100; // List 最多存 100 个 1MB 对象

        while (true) {
            // 分配 1MB 的字节数组
            byte[] chunk = new byte[1024 * 1024];
            memoryHog.add(chunk);

            // 当 List 超过 maxSize，移除旧对象，模拟对象死亡
            if (memoryHog.size() > maxSize) {
                memoryHog.remove(0); // 移除最早的对象，让 GC 可回收
            }

            // 每 50ms 分配一次，控制分配速率
            Thread.sleep(50);

            // 打印内存使用情况，便于观察
            if (memoryHog.size() % 100 == 0) {
                System.out.println("Allocated " + memoryHog.size() + " MB");
            }
        }
    }
}

