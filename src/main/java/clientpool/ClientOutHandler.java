package clientpool;

import io.netty.channel.*;

public class ClientOutHandler extends ChannelOutboundHandlerAdapter {



    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        //System.out.println("writer写出去了"+msg);
        super.write(ctx, msg, promise);
       /* promise.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if(future.isSuccess()){
                    System.out.println("已经发出去了...");
                }
            }
        });
        System.out.println("writer写方法执行了...");*/
    }
}
