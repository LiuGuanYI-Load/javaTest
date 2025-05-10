public class SimpleClass {
    private int test = 0;
    static private  int staticTest = 1 ;
    static{
        System.out.println("static code block...");
    }
    {
        System.out.println("normal code block...");
    }

    public SimpleClass(){

    }
    public static void StaticTest(){
        System.out.println("static test method...");
    }
}
