package clientpool;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CommonLock {

    public Lock lock = new ReentrantLock();

    public Condition writeCondition = lock.newCondition();

    public Condition readCondition = lock.newCondition();

    public volatile boolean sendFlag = true;

}
