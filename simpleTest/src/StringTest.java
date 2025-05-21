public class StringTest {
    public static void main(String[] args) {
        //只在堆之中有 不加入常量池
        String b = "2";
        String b1 = "2";
        String a = new String("1");
        String a1 = new String("2");
        System.out.println(b1 == b);
        System.out.println(b == a1);

    }
}
