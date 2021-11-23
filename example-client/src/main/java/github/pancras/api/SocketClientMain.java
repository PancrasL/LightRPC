package github.pancras.api;

import github.pancras.HelloService;
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
        RpcClient rpcClient = new SocketRpcClient();
        RpcReferenceConfig<HelloService> referenceConfig = new RpcReferenceConfig
                .Builder<>(rpcClient, HelloService.class)
                .build();
        for (int i = 0; i < 5; i++) {
            HelloService helloService = referenceConfig.getReferent();
            String s = helloService.hello("Good, socket transport is success.");
            System.out.println(i + s);
        }
        rpcClient.destroy();
    }
}
