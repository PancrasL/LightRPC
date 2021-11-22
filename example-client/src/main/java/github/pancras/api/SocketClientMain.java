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
    public static void main(String[] args) throws Exception {
        RpcClient rpcClient = new SocketRpcClient();
        RpcReferenceConfig<HelloService> rpcReferenceConfig = new RpcReferenceConfig.Builder<>(rpcClient, HelloService.class).build();
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 50000000; i++) {
            HelloService helloService = rpcReferenceConfig.getReferent();
            String s = helloService.hello("Good, socket transport is success.");
            System.out.println(i);
        }
        long endTime = System.currentTimeMillis();
        System.out.println((endTime - startTime));
        rpcClient.destroy();
    }
}
