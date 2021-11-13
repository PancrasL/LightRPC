package github.pancras;

import github.pancras.remoting.transport.netty.server.NettyRpcServer;
import github.pancras.serviceimpl.HelloServiceImpl;
import github.pancras.wrapper.RpcServiceConfig;

/**
 * @author pancras
 */
public class NettyServerMain {
    public static void main(String[] args) throws Exception {
        RpcServiceConfig rpcServiceConfig = new RpcServiceConfig(new HelloServiceImpl(), "default_group", "latest");

        NettyRpcServer nettyRpcServer = new NettyRpcServer();
        nettyRpcServer.registerService(rpcServiceConfig);
        try {
            nettyRpcServer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
