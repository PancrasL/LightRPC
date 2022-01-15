package github.pancras.api;

import github.pancras.HelloService;
import github.pancras.config.DefaultConfig;
import github.pancras.remoting.transport.RpcClient;
import github.pancras.remoting.transport.netty.client.NettyRpcClient;
import github.pancras.wrapper.RpcReferenceConfig;

/**
 * @author pancras
 */
public class NettyClientMain {
    public static void main(String[] args) {
        RpcClient rpcClient = new NettyRpcClient(DefaultConfig.REGISTRY);
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

        // 指定serviceName调用
        RpcReferenceConfig<HelloService> config = new
                RpcReferenceConfig.Builder<HelloService>(rpcClient, HelloService.class)
                .serviceName("github.pancras.HelloService")
                .group("group1")
                .version("version1")
                .build();
        HelloService service = config.getReferent();
        String s = service.hello("Do");
        System.out.println(s);
    }
}
