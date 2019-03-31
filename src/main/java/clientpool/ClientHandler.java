package clientpool;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.atomic.AtomicInteger;

public class ClientHandler extends SimpleChannelInboundHandler<String> {

    private AtomicInteger atomicInteger = new AtomicInteger(0);

    public CommonLock commonLock;

    public ClientHandler(CommonLock commonLock){

        this.commonLock = commonLock;
    }
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        commonLock.lock.lock();
        commonLock.sendFlag = true;
        System.out.println(msg);
        commonLock.writeCondition.signal();
        commonLock.lock.unlock();

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel客户端连接成功了...");
    }
}
