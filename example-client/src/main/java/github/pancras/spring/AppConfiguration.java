package github.pancras.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import github.pancras.remoting.transport.RpcClient;
import github.pancras.remoting.transport.netty.client.NettyRpcClient;

/**
 * 配置类，声明必要的Bean
 *
 * @author PancrasL
 */
@Configuration
public class AppConfiguration {
    @Bean
    public NettyRpcClient nettyRpcClient() {
        return new NettyRpcClient("zookeeper://localhost:2181");
    }

    @Bean
    public RpcReferencePostProcessor rpcReferencePostProcessor(RpcClient rpcClient) {
        return new RpcReferencePostProcessor(rpcClient);
    }
}
