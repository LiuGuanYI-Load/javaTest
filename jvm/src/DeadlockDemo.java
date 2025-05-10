public class DeadlockDemo {
    public static void main(String[] args) {
        Object lock1 = new Object();
        Object lock2 = new Object();

        new Thread(() -> {
            synchronized (lock1) {
                try { Thread.sleep(100); } catch (Exception e) {}
                synchronized (lock2) {
                    System.out.println("Thread 1 acquired both locks");
                }
            }
        }).start();

        new Thread(() -> {
            synchronized (lock2) {
                try { Thread.sleep(100); } catch (Exception e) {}
                synchronized (lock1) {
                    System.out.println("Thread 2 acquired both locks");
                }
            }
        }).start();
    }
}
