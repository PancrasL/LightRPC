package github.pancras.remoting.transport.netty.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.Nonnull;

import github.pancras.commons.ShutdownHook;
import github.pancras.commons.utils.SystemUtil;
import github.pancras.provider.ProviderFactory;
import github.pancras.provider.ProviderService;
import github.pancras.registry.RegistryFactory;
import github.pancras.remoting.transport.RpcServer;
import github.pancras.remoting.transport.netty.codec.Decoder;
import github.pancras.remoting.transport.netty.codec.Encoder;
import github.pancras.wrapper.RpcServiceConfig;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author PancrasL
 */
public class NettyRpcServer implements RpcServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(NettyRpcServer.class);

    private InetSocketAddress address;
    private final ServerBootstrap serverBootstrap = new ServerBootstrap();
    /**
     * 一个 accepter线程处理客户端连接 2*cpu个线程处理io cpu个线程处理业务 参考：https://www.cnblogs.com/jpfss/p/11016169.html
     */
    private final EventLoopGroup bossGroup;
    private final EventLoopGroup workerGroup;
    private final DefaultEventLoopGroup serviceHandlerGroup;
    private Channel serverChannel;

    private final ProviderService providerService;
    private final AtomicBoolean initialized = new AtomicBoolean(false);

    public NettyRpcServer(InetSocketAddress address) {
        providerService = ProviderFactory.getInstance();
        // 监听线程组，监听客户端请求
        bossGroup = new NioEventLoopGroup(1);
        // 工作线程组，处理与客户端的数据通讯
        workerGroup = new NioEventLoopGroup(SystemUtil.getAvailableProcessorNum() * 2);
        // 业务线程，处理业务逻辑
        serviceHandlerGroup = new DefaultEventLoopGroup(SystemUtil.getAvailableProcessorNum());
    }

    @Override
    public void registerService(@Nonnull RpcServiceConfig<?> rpcServiceConfig) throws Exception {
        providerService.publishService(rpcServiceConfig);
    }

    @Override
    public void start() throws Exception {
        if (initialized.get()) {
            throw new Exception("The server is already started, please do not start the service repeatedly.");
        }

        this.serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                // 开启TCP心跳机制
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                // 开启Nagle算法，尽量使用大数据块传输
                .childOption(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_BACKLOG, 64)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ChannelPipeline p = ch.pipeline();
                        p.addLast(new Decoder());
                        p.addLast(new Encoder());
                        p.addLast(serviceHandlerGroup, new NettyRpcServerHandler(providerService));
                    }
                });

        // 绑定端口，同步等待
        serverChannel = serverBootstrap.bind(address).sync().channel();
        LOGGER.info("Server is started, listen at [{}]", address);
        initialized.set(true);

        // 添加关闭钩子，在程序退出时调用 destroy() 释放资源
        ShutdownHook.getInstance().addDisposable(this);
    }

    @Override
    public void destroy() {
        try {
            if (initialized.get()) {
                RegistryFactory.getInstance().close();
                serverChannel.close();
            }
            this.bossGroup.shutdownGracefully();
            this.workerGroup.shutdownGracefully();
            this.serviceHandlerGroup.shutdownGracefully();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }
}
