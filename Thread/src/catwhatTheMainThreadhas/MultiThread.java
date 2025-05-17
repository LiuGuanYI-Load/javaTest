package catwhatTheMainThreadhas;

public class MultiThread{
    public static void main(String[] args) throws  InterruptedException{
// 启动3个线程
        for (int i = 1; i <= 3; i++) {
            Thread thread = new Thread(new MyTask(i), "Thread-" + i);
            thread.start();
        }

        // 打印所有线程
        Thread.getAllStackTraces().keySet().forEach(thread ->
                System.out.printf("Thread: %s, Daemon: %b, State: %s%n",
                        thread.getName(), thread.isDaemon(), thread.getState())
        );
        // 让main线程等待，观察线程
        Thread.sleep(99999000);
    }

}
class MyTask implements Runnable {
    private int id;
    public MyTask(int id) { this.id = id; }
    @Override
    public void run() {
        try {
            Thread.sleep(500); // 模拟任务
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}