package github.pancras.api;

import github.pancras.HelloService;
import github.pancras.proxy.RpcClientProxy;
import github.pancras.remoting.transport.RpcClient;
import github.pancras.remoting.transport.netty.client.NettyRpcClient;
import github.pancras.wrapper.RpcServiceConfig;

/**
 * @author pancras
 */
public class NettyClientMain {
    public static void main(String[] args) {
        RpcClient rpcClient = new NettyRpcClient();
        RpcServiceConfig config = new RpcServiceConfig(null, "group1", "v1");
        RpcClientProxy rpcClientProxy = new RpcClientProxy(rpcClient, config);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 5; i++) {
            String s = helloService.hello("Good, netty transport is success.");
            System.out.println(s);
        }
        long endTime = System.currentTimeMillis();
        System.out.println((endTime - startTime));
    }
}
