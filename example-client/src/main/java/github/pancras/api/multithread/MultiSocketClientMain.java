package github.pancras.api.multithread;

import github.pancras.HelloService;
import github.pancras.config.DefaultConfig;
import github.pancras.remoting.transport.RpcClient;
import github.pancras.remoting.transport.socket.SocketRpcClient;
import github.pancras.wrapper.RpcReferenceConfig;

/**
 * @author pancras
 * <p>
 * Socket客户端多线程测试
 */
public class MultiSocketClientMain implements Runnable {
    public static RpcClient rpcClient;

    public static void main(String[] args) throws InterruptedException {
        RpcClient rpcClient = new SocketRpcClient(DefaultConfig.REGISTRY);
        Thread t1 = new Thread(new MultiSocketClientMain());
        Thread t2 = new Thread(new MultiSocketClientMain());
        Thread t3 = new Thread(new MultiSocketClientMain());
        t1.start();
        t2.start();
        t3.start();
        t1.join();
        t2.join();
        t3.join();
        rpcClient.destroy();
    }

    @Override
    public void run() {
        System.out.println("线程" + Thread.currentThread() + "已创建");
        RpcReferenceConfig<HelloService> referenceConfig = RpcReferenceConfig.newDefaultConfig(rpcClient, HelloService.class);
        int sendTimes = 5;
        for (int i = 0; i < sendTimes; i++) {
            HelloService helloService = referenceConfig.getReferent();
            String s = helloService.hello("Good, socket transport is success.");
            System.out.printf("线程%s第%d次调用结果：%s\n", Thread.currentThread(), i, s);
        }
    }
}
