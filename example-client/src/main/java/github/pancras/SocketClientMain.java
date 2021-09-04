package github.pancras;

import github.pancras.proxy.RpcClientProxy;
import github.pancras.remoting.transport.RpcClient;
import github.pancras.remoting.transport.socket.SocketRpcClient;

/**
 * @author pancras
 * @create 2021/6/3 18:54
 * <p>
 * RPC调用底层采用Socket数据传输的客户端实现
 */
public class SocketClientMain {
    public static void main(String[] args) {
        RpcClient rpcClient = new SocketRpcClient();
        RpcClientProxy rpcClientProxy = new RpcClientProxy(rpcClient);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            String s = helloService.hello("good");
        }
        long endTime = System.currentTimeMillis();
        System.out.println((endTime - startTime));
        rpcClient.destroy();
    }
}
