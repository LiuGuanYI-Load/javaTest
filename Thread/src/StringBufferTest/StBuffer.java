package StringBufferTest;

public class StBuffer {
    public static void main(String[] args) {
        StringBuilder st = new StringBuilder("st Test");
//        String s = "st Test";


/*        这里tostring 是使用的 new String ，所以不会放进字符串常量池
        public String toString() {
            return this.isLatin1() ? StringLatin1.newString(this.value, 0, this.count) : StringUTF16.newString(this.value, 0, this.count);
        }*/

        String s2= st.toString();
//        System.out.println(s == st.toString());
//        System.out.println(s2 == s);
        String s3 = "st Test".intern();
        System.out.println(s2 == s3);

        /**
        * @Author: Jav
        * @Date: 2025/5/12
        * @Description:
        * @Param: [java.lang.String[]]
        * @return: void
        * second test  for  ---> new string()
        */
        String t1 = new String("t");
        String t2 = "t".intern();
        String t3 = "t";
        System.out.println(t1 == t2);
        System.out.println(t2 == t3);

    }
}
