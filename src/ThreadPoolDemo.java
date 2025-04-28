import java.sql.SQLOutput;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadPoolDemo {
    // 模拟客户任务的Runnable实现
    static class CustomerTask implements Runnable {
        private final int customerId;

        CustomerTask(int id) {
            this.customerId = id;
        }

        @Override
        public void run() {
            try {
                System.out.println(Thread.currentThread().getName() + " 正在服务客户#" + customerId);
                Thread.sleep(3000); // 模拟业务办理时间
                System.out.println("√ 客户#" + customerId + " 业务办理完成");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        // 阶段1：基础线程池
        //demo1_basicPool();

        // 阶段2：队列容量影响
        //demo2_queueCapacity();

        // 阶段3：最大线程数
        //demo3_maxPoolSize();

        // 阶段4：存活时间
       // demo4_keepAlive();

        // 阶段5：拒绝策略
        //demo5_rejectionPolicy();

        // 阶段6：线程工厂
       // demo6_threadFactory();

        // 阶段7：完整参数组合
        demo7_fullConfiguration();
    }

    /* 阶段1：核心线程数与立即执行 */
    private static void demo1_basicPool() throws InterruptedException {
        System.out.println("\n==== 阶段1：基础营业厅（核心线程数） ====");
        // 类比：银行有2个固定窗口
        ExecutorService pool = new ThreadPoolExecutor(
                2, // 核心线程数
                2, // 最大线程数（与核心相同）
                1, TimeUnit.MINUTES,
                new SynchronousQueue<>() // 直接传递队列
                //new LinkedBlockingQueue<>(1)
        );

        // 模拟5个客户到达
        for (int i = 1; i <= 5; i++) {
            System.out.println("客户#" + i + " 到达银行");
            try {
                pool.execute(new CustomerTask(i));
            } catch (RejectedExecutionException e) {
                System.out.println("× 客户#" + i + " 被拒绝进入（队列已满）");
            }
            Thread.sleep(1000); // 模拟客户到达间隔
        }
        pool.shutdown();
    }

    /* 阶段2：队列容量影响 */
    private static void demo2_queueCapacity() throws InterruptedException {
        System.out.println("\n==== 阶段2：等候区扩展（队列容量） ====");
        // 类比：增加3个等候座位
        ExecutorService pool = new ThreadPoolExecutor(
                2, // 核心窗口
                2,
                0, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(3) // 容量3的队列
        );

        for (int i = 1; i <= 6; i++) {
            System.out.println("客户#" + i + " 到达银行");
            try {
                pool.execute(new CustomerTask(i));
            } catch (RejectedExecutionException e) {
                System.out.println("× 客户#" + i + " 被拒绝（等候区满）");
            }
            Thread.sleep(100);
        }
        pool.shutdown();
    }

    /* 阶段3：最大线程数（临时窗口） */
    private static void demo3_maxPoolSize() throws InterruptedException {
        System.out.println("\n==== 阶段3：开启临时窗口（最大线程数） ====");
        ExecutorService pool = new ThreadPoolExecutor(
                2, // 核心窗口
                4, // 最大窗口（可开2个临时）
                0, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(3) // 队列容量3
        );

        for (int i = 1; i <= 9; i++) {
            System.out.println("客户#" + i + " 到达银行");
            try {
                pool.execute(new CustomerTask(i));
            } catch (RejectedExecutionException e) {
                System.out.println("× 客户#" + i + " 被拒绝（超过接待能力）");
            }
            Thread.sleep(100);
        }
        pool.shutdown();
    }

    /* 阶段4：线程存活时间 */
    private static void demo4_keepAlive() throws InterruptedException {
        System.out.println("\n==== 阶段4：临时窗口关闭策略（线程存活时间） ====");
        ThreadPoolExecutor pool = new ThreadPoolExecutor(
                2,
                4,
                2, TimeUnit.SECONDS, // 临时窗口空闲2秒后关闭
                new ArrayBlockingQueue<>(3)
        );

        // 提交6个任务
        for (int i = 1; i <= 10; i++) {
            pool.execute(new CustomerTask(i));

        }

        // 监控线程池状态
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(100);
                    System.out.println("[监控] 当前活跃窗口数：" + pool.getPoolSize());
                    if(pool.getPoolSize()==0)
                    {return;}
                } catch (InterruptedException e) {
                    break;
                }
            }
        }).start();

        Thread.sleep(10000);
        pool.shutdown();
    }

    /* 阶段5：拒绝策略 */
    private static void demo5_rejectionPolicy() throws InterruptedException {
        System.out.println("\n==== 阶段5：客满处理策略（拒绝策略） ====");
        ThreadPoolExecutor pool = new ThreadPoolExecutor(
                2,
                4,
                2, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(3),
                new ThreadPoolExecutor.AbortPolicy() // 默认策略，可替换其他策略
        );

        // 替换拒绝策略示例：
        // new ThreadPoolExecutor.CallerRunsPolicy()  // 由提交线程自己执行
        // new ThreadPoolExecutor.DiscardOldestPolicy()// 丢弃最旧任务
        // new ThreadPoolExecutor.DiscardPolicy()      // 静默丢弃

        for (int i = 1; i <= 10; i++) {
            try {
                pool.execute(new CustomerTask(i));
                System.out.println("客户#" + i + " 进入银行");
            } catch (RejectedExecutionException e) {
                System.out.println("× 客户#" + i + " 被拒绝：" + e.getClass().getSimpleName());
            }
            Thread.sleep(100);
        }
        pool.shutdown();
    }

    /* 阶段6：线程工厂（自定义窗口样式） */
    private static void demo6_threadFactory() throws InterruptedException {
        System.out.println("\n==== 阶段6：VIP服务窗口（线程工厂） ====");
        ThreadPoolExecutor pool = new ThreadPoolExecutor(
                2, 4, 2, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(3),
                new CustomerThreadFactory() // 自定义线程生成器
        );

        for (int i = 1; i <= 10; i++) {
            pool.execute(new CustomerTask(i));
            Thread.sleep(100);
        }
        pool.shutdown();
    }

    /* 阶段7：完整参数组合 */
    private static void demo7_fullConfiguration() throws InterruptedException {
        System.out.println("\n==== 阶段7：旗舰营业厅（完整配置） ====");
        ThreadPoolExecutor pool = new ThreadPoolExecutor(
                4, // 核心窗口
                8, // 最大窗口
                30, TimeUnit.SECONDS, // 长时间空闲关闭临时窗口
                new LinkedBlockingQueue<>(10), // 较大等候区
                new CustomerThreadFactory(),
                new ThreadPoolExecutor.CallerRunsPolicy() // 饱和时由主线程处理
        );

        // 提交20个客户
        for (int i = 1; i <= 20; i++) {
            final int clientId = i;
            try {
                pool.execute(new CustomerTask(clientId));
                System.out.println("客户#" + clientId + " 进入银行");
            } catch (RejectedExecutionException e) {
                System.out.println("× 客户#" + clientId + " 被拒绝");
            }
            Thread.sleep(200);
        }
        pool.shutdown();
    }

    /* 自定义线程工厂（给线程命名） */
    static class CustomerThreadFactory implements ThreadFactory {
        private final AtomicInteger counter = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r, "VIP窗口-" + counter.getAndIncrement());
            t.setDaemon(false);
            t.setPriority(Thread.NORM_PRIORITY);
            return t;
        }
    }
    static class CustomRejectException extends RejectedExecutionException{


    }
    //No interface expected here 这儿不需要接口的意思啊哈哈
    static class CustomRejectPolicy implements  RejectedExecutionHandler{
        @Override
        public void rejectedExecution(Runnable runnable, ThreadPoolExecutor threadPoolExecutor) {

        }
    }
}