package github.pancras.remoting.transport.netty.server;

import github.pancras.config.RpcServiceConfig;
import github.pancras.config.ServerConfig;
import github.pancras.factory.SingletonFactory;
import github.pancras.provider.ServiceProvider;
import github.pancras.provider.impl.ZkServiceProviderImpl;
import github.pancras.remoting.dto.RpcRequest;
import github.pancras.remoting.transport.netty.codec.Decoder;
import github.pancras.remoting.transport.netty.codec.Encoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @author pancras
 * @create 2021/6/15 16:28
 */
public class NettyRpcServer {

    private final ServiceProvider serviceProvider;
    private final String host = ServerConfig.SERVER_ADDRESS;
    private final int port = ServerConfig.PORT;

    public NettyRpcServer() {
        this.serviceProvider = SingletonFactory.getInstance(ZkServiceProviderImpl.class);
    }

    public void registerService(RpcServiceConfig rpcServiceConfig) {
        serviceProvider.publishService(rpcServiceConfig);
    }

    public void start() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        DefaultEventLoopGroup serviceHandlerGroup = new DefaultEventLoopGroup(4);

        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                // Enable TCP heartbeat
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                // Enable Nagle algorithm, to send big data chunk
                .childOption(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_BACKLOG, 64)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ChannelPipeline p = ch.pipeline();
                        p.addLast(new Decoder(RpcRequest.class));
                        p.addLast(new Encoder());
                        p.addLast(serviceHandlerGroup, new NettyRpcRequestHandler());
                    }
                });

        // Bind port
        ChannelFuture f = b.bind(host, port).sync();
        // Wait for the server-side listening port to close
        f.channel().closeFuture().sync();

        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
        serviceHandlerGroup.shutdownGracefully();
    }
}
