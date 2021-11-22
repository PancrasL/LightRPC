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
        RpcReferenceConfig<HelloService> rpcReferenceConfig = new RpcReferenceConfig.Builder<>(rpcClient, HelloService.class).build();
        HelloService helloService = rpcReferenceConfig.getReferent();
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 5; i++) {
            String s = helloService.hello("Good, socket transport is success.");
            System.out.println(s);
        }
        long endTime = System.currentTimeMillis();
        System.out.println((endTime - startTime));
        rpcClient.destroy();
    }
}
