package client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.AttributeKey;

import java.util.concurrent.atomic.AtomicInteger;

public class NettyClient {

    public void connect(CommonMethod commonMethod) throws InterruptedException {
        EventLoopGroup loopGroup = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();

        try {
            bootstrap.group(loopGroup)
                     .channel(NioSocketChannel.class)
                     .handler(new ChannelInitializer<NioSocketChannel>() {
                         @Override
                         protected void initChannel(NioSocketChannel ch) throws Exception {
                             ChannelPipeline pipeline = ch.pipeline();
                             pipeline.addLast(new FixedLengthFrameDecoder(6));
                             pipeline.addLast(new StringDecoder());
                             pipeline.addLast(new StringEncoder());
                             pipeline.addLast(new ClientHandler(commonMethod));
                         }
                     });
            //bootstrap.connect("127.0.0.1",9999);
            ChannelFuture future = bootstrap.connect("127.0.0.1",9999).sync();
            future.channel().closeFuture().sync();
        }finally {

            loopGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup loopGroup = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();

        bootstrap.attr(AttributeKey.newInstance("nio"),new AtomicInteger(1));
        try {
            bootstrap.group(loopGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new FixedLengthFrameDecoder(6));
                            pipeline.addLast(new StringDecoder());
                            pipeline.addLast(new StringEncoder());
                            //pipeline.addLast(new ClientHandler(commonMethod));
                            pipeline.addLast(new ClientHandler1());
                        }
                    });
            //bootstrap.connect("127.0.0.1",9999);
            ChannelFuture future = bootstrap.connect("127.0.0.1",8888).sync();
            future.channel().closeFuture().sync();
        }finally {

            loopGroup.shutdownGracefully();
        }
    }
}
