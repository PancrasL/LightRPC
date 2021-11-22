package github.pancras.api;

import java.net.InetSocketAddress;

import github.pancras.api.service.HelloServiceImpl;
import github.pancras.remoting.transport.netty.server.NettyRpcServer;
import github.pancras.wrapper.RpcServiceConfig;

/**
 * @author pancras
 */
public class NettyServerMain {
    public static void main(String[] args) throws Exception {
        NettyRpcServer nettyRpcServer = new NettyRpcServer(InetSocketAddress.createUnresolved("localhost", 7998));
        HelloServiceImpl serviceInstance = new HelloServiceImpl();
        RpcServiceConfig<HelloServiceImpl> rpcServiceConfig = new RpcServiceConfig.Builder<>(nettyRpcServer, serviceInstance).build();

        // 暴露及注册服务
        rpcServiceConfig.export();
        try {
            nettyRpcServer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
