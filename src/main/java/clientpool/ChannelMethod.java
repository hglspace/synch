package clientpool;

import io.netty.channel.Channel;
import io.netty.channel.pool.FixedChannelPool;

import java.util.concurrent.atomic.AtomicInteger;

public class ChannelMethod {

    private AtomicInteger atomicInteger = new AtomicInteger(0);
    private FixedChannelPool fixedChannelPool;

    public ChannelMethod(FixedChannelPool fixedChannelPool){
        this.fixedChannelPool = fixedChannelPool;
    }


    public void sendMessage(Channel channel, CommonLock commonLock, String message){
        commonLock.lock.lock();
        while (!commonLock.sendFlag){
            try {
                commonLock.writeCondition.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        commonLock.sendFlag = false;
        channel.writeAndFlush(message);
        System.out.println(Thread.currentThread().getName() + "发送:"+message);
        commonLock.lock.unlock();
    }
}
