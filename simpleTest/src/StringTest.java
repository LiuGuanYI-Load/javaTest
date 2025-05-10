public class StringTest {
    public static void main(String[] args) {
        //只在堆之中有 不加入常量池
        String a = new String("");
        String a1 = new String("");
        //加入常量池
        String b = "";
        String b1 = "";
        System.out.println(b == b1);
        System.out.println(a == a1);
    }
}
