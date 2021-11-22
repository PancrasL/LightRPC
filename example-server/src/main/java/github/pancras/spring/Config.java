package github.pancras.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;

import github.pancras.config.DefaultConfig;
import github.pancras.remoting.transport.netty.server.NettyRpcServer;

@Configuration
public class Config {

    @Bean
    public NettyRpcServer nettyRpcServer() {
        return new NettyRpcServer(InetSocketAddress.createUnresolved(DefaultConfig.DEFAULT_SERVER_ADDRESS, DefaultConfig.DEFAULT_SERVER_PORT));
    }
}
