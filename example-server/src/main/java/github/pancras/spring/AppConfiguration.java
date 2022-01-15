package github.pancras.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
        return new NettyRpcServer("localhost:7998", "zookeeper://localhost:2181");
    }

    @Bean
    public RpcServicePostProcessor rpcServiceConfig(RpcServer rpcServer) {
        return new RpcServicePostProcessor(rpcServer);
    }
}
