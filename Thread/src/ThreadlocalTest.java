public class ThreadlocalTest {
    public static void main(String[] args) {
        ThreadLocal tt=new ThreadLocal<>();
        tt.set("23");
        System.out.println(tt.get());
        tt.set("sdf");
        System.out.println(tt.get());
        System.out.println(Thread.currentThread().getName());
        System.out.println(Thread.currentThread().getContextClassLoader());
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        System.out.println(Thread.currentThread().getContextClassLoader().getParent());
                    }
                }
    ).start();
        new Thread(()->{
            System.out.println(Thread.currentThread().getName());
        }).start();

    }
}
