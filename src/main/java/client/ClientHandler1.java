package client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ClientHandler1 extends SimpleChannelInboundHandler<String> {


   /* private volatile boolean s = false;
    private CommonMethod commonMethod;
    public ClientHandler1(CommonMethod commonMethod){
        this.commonMethod = commonMethod;

    }*/
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println(msg);


    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端连接成功....");
        System.out.println("active方法。。。");
        ctx.writeAndFlush("client");
    }
}
