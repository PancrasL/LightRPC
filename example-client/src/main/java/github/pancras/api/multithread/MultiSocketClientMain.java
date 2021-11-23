package github.pancras.api.multithread;

import github.pancras.HelloService;
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
        rpcClient = new SocketRpcClient();
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
        RpcReferenceConfig<HelloService> rpcReferenceConfig = new RpcReferenceConfig
                .Builder<>(rpcClient, HelloService.class)
                .build();
        for (int i = 0; i < 5; i++) {
            HelloService helloService = rpcReferenceConfig.getReferent();
            String s = helloService.hello("Good, socket transport is success.");
            System.out.printf("线程%s第%d次调用结果：%s\n", Thread.currentThread(), i, s);
        }
    }
}
