import java.util.concurrent.*;

public class SubmitVSExecute {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        // 使用execute
        executor.execute(() -> {
            System.out.println("Execute task running");
            // 异常会直接抛出
            // throw new RuntimeException("Execute exception");
        });

        // 使用submit with Runnable
        Future<?> future1 = executor.submit(() -> {
            System.out.println("Submit Runnable task running");
            // 异常被封装在Future中
             throw new RuntimeException("Submit Runnable exception");
        });

        // 使用submit with Callable
        Future<String> future2 = executor.submit(() -> {
            System.out.println("Submit Callable task running");
            return "Callable result";
            // throw new RuntimeException("Submit Callable exception");
        });

        // 使用submit with Runnable and result
        Future<String> future3 = executor.submit(() -> {
            System.out.println("Submit Runnable with result running");
        }, "Predefined result");

        try {
            System.out.println("Future1 get: " + future1.get());
            System.out.println("Future2 get: " + future2.get());
            System.out.println("Future3 get: " + future3.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        executor.shutdown();
    }
}