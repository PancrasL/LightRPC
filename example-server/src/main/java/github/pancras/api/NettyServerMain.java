package github.pancras.api;

import github.pancras.api.service.HelloServiceImpl;
import github.pancras.remoting.transport.netty.server.NettyRpcServer;
import github.pancras.wrapper.RpcServiceConfig;

/**
 * @author pancras
 */
public class NettyServerMain {
    public static void main(String[] args) throws Exception {
        RpcServiceConfig<HelloServiceImpl> rpcServiceConfig = RpcServiceConfig.newInstance(new HelloServiceImpl());

        NettyRpcServer nettyRpcServer = new NettyRpcServer();
        nettyRpcServer.registerService(rpcServiceConfig);
        try {
            nettyRpcServer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
