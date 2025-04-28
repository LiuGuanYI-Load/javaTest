

public class Main {
    static int a=0;
    public static void main(String[] args) {
        System.out.println("Hello, World!");

        for(int i=0;i<1000000;i++){
            new Thread(()->{
                System.out.println(a++);
            }).start();
        }
        
    }

}