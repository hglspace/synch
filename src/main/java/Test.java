import client.CommonMethod;
import client.NettyClient;

public class Test {

    public static void main(String[] args) throws InterruptedException {
        CommonMethod commonMethod = new CommonMethod();
        NettyClient nettyClient = new NettyClient();


        new Thread(()->{
            try {
                nettyClient.connect(commonMethod);
            }catch (Exception e){
            }

        }).start();

        while (!commonMethod.isConnected){
            Thread.sleep(1);
        }


        System.out.println("可以发送消息了....");
        for(int i=0;i<10;i++){
            System.out.println("第"+(i+1)+"次调用该方法");
            commonMethod.sendMessage((i+1),"client");
        }


        while (true){

        }


    }
}
