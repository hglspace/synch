package clientpool;

import io.netty.channel.socket.nio.NioSocketChannel;

public class NioChannelDecorate{

    private NioSocketChannel nioSocketChannel;


    private NioChannelDecorate(NioSocketChannel nioSocketChannel){
        this.nioSocketChannel = nioSocketChannel;
    }




}
