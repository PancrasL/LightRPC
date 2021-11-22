package github.pancras.api;

import java.net.InetSocketAddress;

import github.pancras.api.service.HelloServiceImpl;
import github.pancras.remoting.transport.socket.SocketRpcServer;
import github.pancras.wrapper.RpcServiceConfig;

/**
 * @author pancras
 * <p>
 * RPC调用底层采用Socket数据传输的服务器实现
 */
public class SocketServerMain {
    public static void main(String[] args) throws Exception {
        InetSocketAddress address = new InetSocketAddress("localhost", 7998);
        SocketRpcServer socketRpcServer = new SocketRpcServer(address);
        HelloServiceImpl serviceInstance = new HelloServiceImpl();
        RpcServiceConfig<HelloServiceImpl> rpcServiceConfig = new RpcServiceConfig
                .Builder<>(socketRpcServer, serviceInstance)
                .build();
        rpcServiceConfig.export();

        socketRpcServer.start();
    }

}
