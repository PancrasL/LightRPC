package github.pancras;

import github.pancras.proxy.RpcClientProxy;
import github.pancras.remoting.transport.RpcClient;
import github.pancras.remoting.transport.netty.client.NettyRpcClient;

/**
 * @author pancras
 * @create 2021/6/16 10:19
 */
public class NettyClientMain {
    public static void main(String[] args) {
        RpcClient rpcClient = new NettyRpcClient();
        RpcClientProxy rpcClientProxy = new RpcClientProxy(rpcClient);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            String s = helloService.hello("good");
            // System.out.println(s);
        }
        long endTime = System.currentTimeMillis();
        System.out.println((endTime - startTime));
    }
}
