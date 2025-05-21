package noShareLock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class lock {
    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock();
        Condition con = lock.newCondition();
    }
}
