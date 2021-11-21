package github.pancras.api;

import github.pancras.HelloService;
import github.pancras.remoting.transport.RpcClient;
import github.pancras.remoting.transport.netty.client.NettyRpcClient;
import github.pancras.wrapper.RpcReferenceConfig;
import github.pancras.wrapper.ServiceWrapper;

/**
 * @author pancras
 */
public class NettyClientMain {
    public static void main(String[] args) {
        RpcClient rpcClient = NettyRpcClient.getInstance();
        ServiceWrapper wrapper = ServiceWrapper.newInstance(HelloService.class);
        RpcReferenceConfig<HelloService> rpcReferenceConfig = RpcReferenceConfig.newInstance(rpcClient, wrapper);
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
