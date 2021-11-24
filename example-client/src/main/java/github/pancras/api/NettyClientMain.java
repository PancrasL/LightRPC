package github.pancras.api;

import github.pancras.HelloService;
import github.pancras.remoting.transport.RpcClient;
import github.pancras.remoting.transport.netty.client.NettyRpcClient;
import github.pancras.wrapper.RegistryConfig;
import github.pancras.wrapper.RpcReferenceConfig;

/**
 * @author pancras
 */
public class NettyClientMain {
    public static void main(String[] args) {
        RegistryConfig config = RegistryConfig.getDefaultConfig();
        RpcClient rpcClient = NettyRpcClient.getInstance(config);
        RpcReferenceConfig<HelloService> referenceConfig = RpcReferenceConfig.newDefaultConfig(rpcClient, HelloService.class);
        HelloService helloService = referenceConfig.getReferent();

        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 5; i++) {
            String s = helloService.hello("Good, netty transport was success.");
            System.out.println(s);
        }
        long endTime = System.currentTimeMillis();
        System.out.println((endTime - startTime));
    }
}
