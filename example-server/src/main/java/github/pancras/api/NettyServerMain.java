package github.pancras.api;

import java.net.InetSocketAddress;

import github.pancras.api.service.HelloServiceImpl;
import github.pancras.config.DefaultConfig;
import github.pancras.remoting.transport.RpcServer;
import github.pancras.remoting.transport.netty.server.NettyRpcServer;
import github.pancras.wrapper.RegistryConfig;
import github.pancras.wrapper.RpcServiceConfig;

/**
 * @author pancras
 */
public class NettyServerMain {
    public static void main(String[] args) throws Exception {
        // 创建服务实例包装类
        // 默认组和默认版本
        HelloServiceImpl service1 = new HelloServiceImpl();
        RpcServiceConfig<HelloServiceImpl> serviceConfig1 = RpcServiceConfig
                .newDefaultConfig(service1);
        // 指定组和指定版本
        HelloServiceImpl service2 = new HelloServiceImpl();
        RpcServiceConfig<HelloServiceImpl> serviceConfig2 = new RpcServiceConfig
                .Builder<>(service2)
                .group("group1")
                .version("version1")
                .build();

        // 创建服务器
        InetSocketAddress address = new InetSocketAddress("localhost", 7998);
        RegistryConfig config = DefaultConfig.DEFAULT_REGISTRY_CONFIG;
        RpcServer server = NettyRpcServer.getInstance(address, config);

        // 发布服务
        server.registerService(serviceConfig1);
        server.registerService(serviceConfig2);

        // 启动服务器
        server.start();
    }
}
