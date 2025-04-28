import java.util.HashMap;
import java.util.Map;

public class HashMapChaoticDemo {
    public static void main(String[] args) {
        // 初始容量设为 4，增加冲突机会
        HashMap<String, String> map = new HashMap<>(4, 0.75f);

        // 用哈希值差异大的键，语义无关
        System.out.println("插入顺序：");
        String[] keys = {
                "xyz123",
                "!@#",
                "987654"

        };

        for (String key : keys) {
            map.put(key, "value-" + key);
            System.out.println("插入: " + key);
        }

        // 遍历
        System.out.println("\n第一次遍历结果：");
        for (Map.Entry<String, String> entry : map.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}