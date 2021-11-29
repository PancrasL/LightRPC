package github.pancras.api;

import github.pancras.HelloService;
import github.pancras.config.DefaultConfig;
import github.pancras.remoting.transport.RpcClient;
import github.pancras.remoting.transport.netty.client.NettyRpcClient;
import github.pancras.wrapper.RegistryConfig;
import github.pancras.wrapper.RpcReferenceConfig;
import java.util.ServiceLoader;

/**
 * @author pancras
 */
public class NettyClientMain {
    public static void main(String[] args) {
        RegistryConfig config = DefaultConfig.DEFAULT_REGISTRY_CONFIG;
        RpcClient rpcClient = NettyRpcClient.getInstance(config);
        RpcReferenceConfig<HelloService> referenceConfig = RpcReferenceConfig.newDefaultConfig(rpcClient, HelloService.class);
        HelloService helloService = referenceConfig.getReferent();

        long startTime = System.currentTimeMillis();
        int sendTimes = 5;
        for (int i = 0; i < sendTimes; i++) {
            String s = helloService.hello("Good, netty transport was success.");
            System.out.println(s);
        }
        long endTime = System.currentTimeMillis();
        System.out.println((endTime - startTime));
    }
}
