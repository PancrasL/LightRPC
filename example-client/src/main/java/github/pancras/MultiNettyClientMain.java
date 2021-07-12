package github.pancras;

import github.pancras.proxy.RpcClientProxy;
import github.pancras.remoting.transport.RpcClient;
import github.pancras.remoting.transport.netty.client.NettyRpcClient;

/**
 * 多客户端连接测试
 *
 * @author PancrasL
 */
public class MultiNettyClientMain {
    public static void main(String[] args) {
        Client runnable = new Client();
        Thread t1 = new Thread(runnable);
        Thread t2 = new Thread(runnable);
        Thread t3 = new Thread(runnable);
        t1.start();
        t2.start();
        t3.start();
        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.exit(-1);
    }
}

class Client implements Runnable {
    @Override
    public void run() {
        RpcClient rpcClient = new NettyRpcClient();
        RpcClientProxy rpcClientProxy = new RpcClientProxy(rpcClient);
        for (int i = 0; i < 100; i++) {
            HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
            String s = helloService.hello(Thread.currentThread().getName());
            System.out.println(s);
        }
    }
}