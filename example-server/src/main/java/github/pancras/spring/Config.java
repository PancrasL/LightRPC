package github.pancras.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import github.pancras.remoting.transport.netty.server.NettyRpcServer;

@Configuration
public class Config {

    @Bean
    public NettyRpcServer nettyRpcServer() {
        return new NettyRpcServer();
    }
}
