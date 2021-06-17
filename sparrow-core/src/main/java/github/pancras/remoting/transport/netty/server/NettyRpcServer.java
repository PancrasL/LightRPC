package github.pancras.remoting.transport.netty.server;

import github.pancras.commons.factory.SingletonFactory;
import github.pancras.config.RpcServiceConfig;
import github.pancras.config.ServerConfig;
import github.pancras.provider.ServiceProvider;
import github.pancras.provider.impl.ZkServiceProviderImpl;
import github.pancras.remoting.transport.RpcServer;
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
public class NettyRpcServer implements RpcServer {

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
                // 开启TCP心跳机制
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                // 开启Nagle算法，尽量使用大数据块传输
                .childOption(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_BACKLOG, 64)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ChannelPipeline p = ch.pipeline();
                        p.addLast(new Decoder());
                        p.addLast(new Encoder());
                        p.addLast(serviceHandlerGroup, new NettyRpcServerHandler());
                    }
                });

        // 绑定端口
        ChannelFuture f = b.bind(host, port).sync();
        f.channel().closeFuture().sync();

        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
        serviceHandlerGroup.shutdownGracefully();
    }
}
