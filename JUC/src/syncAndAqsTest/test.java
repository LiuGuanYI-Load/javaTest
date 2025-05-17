package syncAndAqsTest;

public class test {
    public static Object obj = new Object();
    public static void main(String[] args) {
        new A().test();
        new A().test();
    }
}

class A{
    static {
        System.out.println("first init!");
    }
//    synchronized(this) {
//
//    }
//    synchronized (A.class){
//
//    }
    public void test(){
        synchronized (A.class){
            System.out.println("A.class");
            System.out.println(A.class);
        }
        synchronized (this){
            System.out.println("this");
            System.out.println(this);
        }
        synchronized (test.obj){
            System.out.println("test.obj");
            System.out.println(test.obj);
            System.out.println(test.obj.toString());
            System.out.println(test.obj.getClass());
        }

        {
            System.out.println("...");
        }
    }
}