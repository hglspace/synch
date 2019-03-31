package client;

import io.netty.channel.Channel;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CommonMethod {

    //private AtomicInteger atomicInteger = new AtomicInteger(0);
    public Lock lock = new ReentrantLock();

    public Condition sendCondition = lock.newCondition();

    public Condition receiveCondition = lock.newCondition();

    public volatile boolean channelFlag = true;


    public volatile Channel channel;

    public volatile boolean isConnected = false;

    public void sendMessage(int i,String message) throws InterruptedException {
        lock.lock();
        while (!channelFlag){
            sendCondition.await();
        }
        //channel.writeAndFlush(message);
        channelFlag = false;
        channel.writeAndFlush(message);
        receiveCondition.signal();
        lock.unlock();


        /*while (!channelFlag){
            System.out.println(i + "：休眠...");
            TimeUnit.SECONDS.sleep(1);

        }
        System.out.println(i+":开始发送消息");
        channelFlag = false;
        channel.writeAndFlush(message);*/
    }
}
