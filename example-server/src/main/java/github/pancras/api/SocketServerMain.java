package github.pancras.api;

import github.pancras.wrapper.RpcServiceConfig;
import github.pancras.remoting.transport.socket.SocketRpcServer;
import github.pancras.serviceimpl.HelloServiceImpl;

/**
 * @author pancras
 * <p>
 * RPC调用底层采用Socket数据传输的服务器实现
 */
public class SocketServerMain {
    public static void main(String[] args) {
        RpcServiceConfig rpcServiceConfig = new RpcServiceConfig();
        rpcServiceConfig.setService(new HelloServiceImpl());

        SocketRpcServer socketRpcServer = new SocketRpcServer();
        socketRpcServer.registerService(rpcServiceConfig);
        try {
            socketRpcServer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
