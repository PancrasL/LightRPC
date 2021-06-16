package github.pancras;

import github.pancras.config.RpcServiceConfig;
import github.pancras.remoting.transport.netty.server.NettyRpcServer;
import github.pancras.serviceimpl.HelloServiceImpl;

/**
 * @author pancras
 * @create 2021/6/15 16:24
 */
public class NettyServerMain {
    public static void main(String[] args) {
        RpcServiceConfig rpcServiceConfig = new RpcServiceConfig();
        rpcServiceConfig.setService(new HelloServiceImpl());

        NettyRpcServer nettyRpcServer = new NettyRpcServer();
        nettyRpcServer.registerService(rpcServiceConfig);
        try {
            nettyRpcServer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
