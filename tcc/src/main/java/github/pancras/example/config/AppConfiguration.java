package github.pancras.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import github.pancras.remoting.transport.RpcClient;
import github.pancras.remoting.transport.netty.client.NettyRpcClient;
import github.pancras.spring.RpcReferencePostProcessor;
import github.pancras.wrapper.RegistryConfig;

@Configuration
public class AppConfiguration {
    @Bean
    public NettyRpcClient nettyRpcClient() {
        return NettyRpcClient.getInstance(RegistryConfig.newConfig("zookeeper", "127.0.0.1", 2181));
    }

    @Bean
    public RpcReferencePostProcessor rpcReferencePostProcessor(RpcClient rpcClient) {
        return new RpcReferencePostProcessor(rpcClient);
    }
}
