package github.pancras.api.multithread;

import java.net.InetSocketAddress;

import github.pancras.api.multithread.service.BlockHelloServiceImpl;
import github.pancras.config.DefaultConfig;
import github.pancras.remoting.transport.RpcServer;
import github.pancras.remoting.transport.netty.server.NettyRpcServer;
import github.pancras.wrapper.RegistryConfig;
import github.pancras.wrapper.RpcServiceConfig;

/**
 * @author pancras
 * <p>
 * Netty服务端多线程测试
 */
public class MultiNettyServerMain {
    public static void main(String[] args) throws Exception {
        // 创建服务实例包装类
        // 默认组和默认版本
        BlockHelloServiceImpl service1 = new BlockHelloServiceImpl();
        RpcServiceConfig<BlockHelloServiceImpl> serviceConfig1 = RpcServiceConfig
                .newDefaultConfig(service1);

        // 创建服务器
        InetSocketAddress address = new InetSocketAddress("localhost", 7998);
        RegistryConfig config = DefaultConfig.DEFAULT_REGISTRY_CONFIG;
        RpcServer server = NettyRpcServer.getInstance(address, config);

        // 发布服务
        server.registerService(serviceConfig1);

        // 启动服务器
        server.start();
    }

}
