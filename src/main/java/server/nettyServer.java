package server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class nettyServer {

    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();

        EventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();

        try{
            serverBootstrap.group(bossGroup,workerGroup)
                           .channel(NioServerSocketChannel.class)
                           .childHandler(new ChannelInitializer<NioSocketChannel>() {
                               @Override
                               protected void initChannel(NioSocketChannel ch) throws Exception {
                                   ChannelPipeline pipeline = ch.pipeline();
                                   pipeline.addLast(new FixedLengthFrameDecoder(6));
                                   pipeline.addLast(new StringDecoder());
                                   pipeline.addLast(new StringEncoder());
                                   pipeline.addLast(new ServerHandler());
                               }
                           });
            ChannelFuture future = serverBootstrap.bind(8888).addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if(future.isSuccess()){
                        System.out.println("服务端启动成功.....");
                    }
                }
            }).sync();
            future.channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}
