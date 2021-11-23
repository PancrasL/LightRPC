package github.pancras.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;

import github.pancras.config.DefaultConfig;
import github.pancras.remoting.transport.RpcServer;
import github.pancras.remoting.transport.netty.server.NettyRpcServer;

/**
 * 配置类，声明必要的Bean
 *
 * @author PancrasL
 */
@Configuration
public class AppConfiguration {
    @Bean
    public NettyRpcServer nettyRpcServer() {
        InetSocketAddress socketAddress = new InetSocketAddress(DefaultConfig.DEFAULT_SERVER_ADDRESS, DefaultConfig.DEFAULT_SERVER_PORT);
        return new NettyRpcServer(socketAddress);
    }

    @Bean
    public RpcServicePostProcessor rpcServiceConfig(RpcServer rpcServer) {
        return new RpcServicePostProcessor(rpcServer);
    }
}
