package clientpool;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.pool.FixedChannelPool;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ClientPool {

    EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
    Bootstrap bootstrap = new Bootstrap();

    InetSocketAddress add = new InetSocketAddress("127.0.0.1",8888);

    FixedChannelPool fixedChannelPool ;


    public void build(){

        //attributeKey = AttributeKey.newInstance("NIOBUF");
        bootstrap.group(eventLoopGroup)
                 .channel(NioSocketChannel.class);
        //bootstrap.attr(attributeKey,new CommonLock());


        fixedChannelPool = new FixedChannelPool(bootstrap.remoteAddress(add),new ClientChannelPoolHandler(),2);
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException, TimeoutException {
        ClientPool clientPool = new ClientPool();

        clientPool.build();
        Channel ch = null;

        Future<Channel> f = clientPool.fixedChannelPool.acquire();
        ch =  f.get(1000, TimeUnit.MILLISECONDS);

        System.out.println(ch);
        CommonLock commonLock = ch.pipeline().get(ClientHandler.class).commonLock;
        System.out.println(commonLock.sendFlag);

        //ch.writeAndFlush("abcdef");
        ChannelMethod channelMethod = new ChannelMethod(clientPool.fixedChannelPool);
        Thread[] threads = new Thread[5];
        for(int i = 0 ;i<5;i++){
            final Channel channel = ch;
            final int x = i;
            threads[i] = new Thread(()->{
                for(int j = 0 ;j< 9;j++){
                    channelMethod.sendMessage(channel,commonLock,j+"clie"+x);
                }

            });

        }

        for(int s = 0;s< 5; s++){
            threads[s].start();
        }
        /*for(int i = 0 ;i< 5;i++){
            final int j = i;
            Future<Channel> f = clientPool.fixedChannelPool.acquire();
            f.addListener(new GenericFutureListener<Future<? super Channel>>() {
                @Override
                public void operationComplete(Future<? super Channel> future) throws Exception {
                    if(future.isSuccess()){

                        Channel ch = (Channel) future.getNow();
                        ClientHandler clientHandler = ch.pipeline().get(ClientHandler.class);
                        clientHandler.commonLock.lock.lock();
                        while (!clientHandler.commonLock.sendFlag){
                            clientHandler.commonLock.writeCondition.await();
                        }
                        clientHandler.commonLock.sendFlag = false;
                        System.out.println("j是:"+j);
                        ch.writeAndFlush(j+"clien");
                        System.out.println("客户端发送的消息:"+j+"clien");
                        clientPool.fixedChannelPool.release(ch);
                        clientHandler.commonLock.lock.unlock();

                    }
                }
            });
        }*/

        /*for(int i = 0 ;i < 3;i++){
            new Thread(()->{
                for(int j = 0 ;j< 3;j++){
                    int s = j;
                    Future<Channel> f = clientPool.fixedChannelPool.acquire();
                    f.addListener(new GenericFutureListener<Future<? super Channel>>() {
                        @Override
                        public void operationComplete(Future<? super Channel> future) throws Exception {
                            if(future.isSuccess()){

                                Channel ch = (Channel) future.getNow();
                                ClientHandler clientHandler = ch.pipeline().get(ClientHandler.class);
                                clientHandler.commonLock.lock.lock();
                                while (!clientHandler.commonLock.sendFlag){
                                    System.out.println(Thread.currentThread().getName() + "休眠..");
                                    clientHandler.commonLock.writeCondition.await();
                                }
                                clientHandler.commonLock.sendFlag = false;
                                ch.writeAndFlush(s+"clien");
                                System.out.println(Thread.currentThread().getName() + "发送的消息:"+s+"clien");
                                clientPool.fixedChannelPool.release(ch);
                                clientHandler.commonLock.lock.unlock();

                            }
                        }
                    });
                }

            }).start();
        }*/

        while (true){

        }
    }
}
