package github.pancras.api.multithread;

import github.pancras.api.multithread.service.BlockHelloServiceImpl;
import github.pancras.config.DefaultConfig;
import github.pancras.remoting.transport.RpcServer;
import github.pancras.remoting.transport.socket.SocketRpcServer;
import github.pancras.wrapper.RpcServiceConfig;

/**
 * Socket服务端多线程测试
 *
 * @author pancras
 */
public class MultiSocketServerMain {
    public static void main(String[] args) throws Exception {
        // 创建服务实例包装类
        // 默认组和默认版本
        BlockHelloServiceImpl service1 = new BlockHelloServiceImpl();
        RpcServiceConfig<BlockHelloServiceImpl> serviceConfig1 = RpcServiceConfig
                .newDefaultConfig(service1);

        // 创建服务器
        RpcServer server = new SocketRpcServer(DefaultConfig.SERVER_ADDRESS, DefaultConfig.REGISTRY);

        // 发布服务
        server.registerService(serviceConfig1);
    }

}
