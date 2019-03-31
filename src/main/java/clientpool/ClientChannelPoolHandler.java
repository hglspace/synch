package clientpool;

import io.netty.channel.Channel;
import io.netty.channel.pool.ChannelPoolHandler;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class ClientChannelPoolHandler implements ChannelPoolHandler {





    @Override
    public void channelReleased(Channel ch) throws Exception {

        //System.out.println("released方法被调用:"+ch);
    }

    @Override
    public void channelAcquired(Channel ch) throws Exception {

        //System.out.println("acquired方法被调用:"+ch);
    }

    @Override
    public void channelCreated(Channel ch) throws Exception {

        //System.out.println("create方法被调用:"+ch);
        NioSocketChannel channel = (NioSocketChannel) ch;

        channel.config().setKeepAlive(true);
        channel.config().setTcpNoDelay(true);
        channel.pipeline()
                .addLast(new FixedLengthFrameDecoder(6))
                .addLast(new StringDecoder())
                .addLast(new StringEncoder())
                .addLast(new ClientOutHandler())
                .addLast(new ClientHandler(new CommonLock()));
    }
}
