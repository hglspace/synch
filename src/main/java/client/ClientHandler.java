package client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.atomic.AtomicInteger;

public class ClientHandler extends SimpleChannelInboundHandler<String> {


    private AtomicInteger atomicInteger = new AtomicInteger(0);


    private CommonMethod commonMethod;
    public ClientHandler(CommonMethod commonMethod){
        this.commonMethod = commonMethod;

    }
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        /*commonMethod.lock.lock();
        while (commonMethod.channelFlag){
            commonMethod.receiveCondition.await();
        }*/
        System.out.println(atomicInteger.incrementAndGet() +":"+ msg);
        //CommonObject.channelFlag = true;
        /*try {
            commonMethod.sendCondition.signal();
        }catch (Exception e){
            System.out.println("第一次唤醒，会报异常...");
        }*/

        /*if(!s){
            s = true;
        }else {
            new Thread(()->{

                commonMethod.lock.lock();
                System.out.println("独立线程开始运行...");
                commonMethod.sendCondition.signal();
                commonMethod.lock.unlock();
            }).start();
        }*/
       // commonMethod.sendCondition.signal();

       /*if(!s){
           s = true;
           System.out.println("运行了1");
       }else {
           System.out.println("运行了2");
           commonMethod.sendCondition.signal();
       }*/

       //ctx.writeAndFlush("123456");
       //commonMethod.lock.unlock();


    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        /*System.out.println("客户端连接成功....");
        System.out.println("active方法。。。");
        ctx.writeAndFlush("client");
        System.out.println("客户端握手结束....");*/
        commonMethod.isConnected = true;
        commonMethod.channel = ctx.channel();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        commonMethod.lock.lock();
        System.out.println("readComplete方法运行了.....");
        commonMethod.channelFlag = true;
        commonMethod.sendCondition.signal();
        commonMethod.lock.unlock();
    }
}
