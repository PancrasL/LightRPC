package github.pancras.api;

import github.pancras.HelloService;
import github.pancras.remoting.transport.RpcClient;
import github.pancras.remoting.transport.netty.client.NettyRpcClient;
import github.pancras.wrapper.RpcReferenceConfig;

/**
 * @author pancras
 */
public class NettyClientMain {
    public static void main(String[] args) {
        RpcClient rpcClient = NettyRpcClient.getInstance();
        RpcReferenceConfig<HelloService> rpcReferenceConfig = new RpcReferenceConfig.Builder<>(rpcClient, HelloService.class).build();
        HelloService helloService = rpcReferenceConfig.getReferent();
      
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 5; i++) {
            String s = helloService.hello("Good, netty transport was success.");
            System.out.println(s);
        }
        long endTime = System.currentTimeMillis();
        System.out.println((endTime - startTime));
    }
}
