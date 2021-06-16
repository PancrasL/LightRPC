package github.pancras;

import github.pancras.proxy.RpcClientProxy;
import github.pancras.remoting.transport.RpcRequestTransport;
import github.pancras.remoting.transport.netty.client.NettyRpcClient;

/**
 * @author pancras
 * @create 2021/6/16 10:19
 */
public class NettyClientMain {
    public static void main(String[] args) {
        RpcRequestTransport rpcRequestTransport = new NettyRpcClient();
        RpcClientProxy rpcClientProxy = new RpcClientProxy(rpcRequestTransport);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        String s = helloService.hello("good");
        System.out.println(s);
    }
}
