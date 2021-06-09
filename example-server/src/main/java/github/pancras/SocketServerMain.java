package github.pancras;

import github.pancras.config.RpcServiceConfig;
import github.pancras.remoting.transport.socket.SocketRpcServer;
import github.pancras.serviceimpl.HelloServiceImpl;

/**
 * @author pancras
 * @create 2021/6/3 18:56
 * <p>
 * RPC调用底层采用Socket数据传输的服务器实现
 */
public class SocketServerMain {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        RpcServiceConfig rpcServiceConfig = new RpcServiceConfig();
        rpcServiceConfig.setService(helloService);

        SocketRpcServer socketRpcServer = new SocketRpcServer();
        socketRpcServer.registerService(rpcServiceConfig);

        socketRpcServer.start();
    }

}
