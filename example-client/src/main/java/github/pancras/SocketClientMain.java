package github.pancras;

import github.pancras.proxy.RpcClientProxy;
import github.pancras.remoting.transport.RpcRequestTransport;
import github.pancras.remoting.transport.socket.SocketRpcClient;

/**
 * @author pancras
 * @create 2021/6/3 18:54
 * <p>
 * RPC调用底层采用Socket数据传输的客户端实现
 */
public class SocketClientMain {
    public static void main(String[] args) {
        RpcRequestTransport rpcRequestTransport = new SocketRpcClient();
        RpcClientProxy rpcClientProxy = new RpcClientProxy(rpcRequestTransport);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        String s = helloService.hello("good");
        System.out.println(s);
    }
}
