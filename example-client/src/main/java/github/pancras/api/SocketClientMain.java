package github.pancras.api;

import github.pancras.HelloService;
import github.pancras.config.DefaultConfig;
import github.pancras.remoting.transport.RpcClient;
import github.pancras.remoting.transport.socket.SocketRpcClient;
import github.pancras.wrapper.RpcReferenceConfig;

/**
 * @author pancras
 * <p>
 * RPC调用底层采用Socket数据传输的客户端实现
 */
public class SocketClientMain {
    public static void main(String[] args) {
        RpcClient rpcClient = new SocketRpcClient(DefaultConfig.REGISTRY);
        RpcReferenceConfig<HelloService> referenceConfig = RpcReferenceConfig.newDefaultConfig(rpcClient, HelloService.class);
        int sendTimes = 5;
        for (int i = 0; i < sendTimes; i++) {
            HelloService helloService = referenceConfig.getReferent();
            String s = helloService.hello("Good, socket transport is success.");
            System.out.println(i + s);
        }
        rpcClient.destroy();
    }
}
