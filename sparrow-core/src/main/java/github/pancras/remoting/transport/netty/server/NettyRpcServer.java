package github.pancras.remoting.transport.netty.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import github.pancras.commons.factory.SingletonFactory;
import github.pancras.commons.utils.SystemUtil;
import github.pancras.config.RpcServiceConfig;
import github.pancras.config.SparrowConfig;
import github.pancras.provider.ServiceProvider;
import github.pancras.provider.impl.ServiceProviderImpl;
import github.pancras.remoting.transport.RpcServer;
import github.pancras.remoting.transport.netty.codec.Decoder;
import github.pancras.remoting.transport.netty.codec.Encoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(NettyRpcServer.class);

    private final ServiceProvider serviceProvider;
    private final String host = SparrowConfig.SERVER_LISTEN_ADDRESS;
    private final int port = SparrowConfig.PORT;

    public NettyRpcServer() {
        this.serviceProvider = SingletonFactory.getInstance(ServiceProviderImpl.class);
    }

    @Override
    public void registerService(RpcServiceConfig rpcServiceConfig) {
        serviceProvider.publishService(rpcServiceConfig);
    }

    @Override
    public void start() throws Exception {
        // 监听线程组，监听客户端请求
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        // 工作线程组，处理与客户端的数据通讯
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        // 业务线程，处理业务逻辑
        DefaultEventLoopGroup serviceHandlerGroup = new DefaultEventLoopGroup(SystemUtil.getAvailableProcessors() * 2);

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

        // 绑定端口 同步等待
        ChannelFuture f = b.bind(host, port).sync();

        // 采用异步的方式退出并释放资源
        f.channel().closeFuture().addListener((ChannelFutureListener) channelFuture -> {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            serviceHandlerGroup.shutdownGracefully();
        });

        LOGGER.info("Server is started, listen at [{}:{}]", host, port);
    }
}
