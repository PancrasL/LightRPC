package github.pancras.remoting.transport.netty.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

import javax.annotation.Nonnull;

import github.pancras.commons.ShutdownHook;
import github.pancras.commons.utils.NetUtil;
import github.pancras.provider.ProviderService;
import github.pancras.provider.impl.ProviderServiceImpl;
import github.pancras.registry.RegistryFactory;
import github.pancras.registry.RegistryService;
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
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * @author PancrasL
 */
public class NettyRpcServer implements RpcServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(NettyRpcServer.class);

    private final ProviderService providerService;
    /**
     * 一个 accepter线程处理客户端连接 2*cpu个线程处理io cpu个线程处理业务 参考：https://www.cnblogs.com/jpfss/p/11016169.html
     */
    private final EventLoopGroup bossGroup;
    private final EventLoopGroup workerGroup;
    private final DefaultEventLoopGroup serviceHandlerGroup;

    private final InetSocketAddress address;
    private final ServerBootstrap serverBootstrap = new ServerBootstrap();
    private Channel serverChannel;

    public NettyRpcServer(String serverAddress, String registryAddress) {
        this.address = NetUtil.toInetSocketAddress(serverAddress);
        // 提供者服务，用于发布和查询服务
        // 注册中心
        RegistryService registryService = RegistryFactory.getRegistry(registryAddress);
        providerService = ProviderServiceImpl.newInstance(registryService);
        // 监听线程组，监听客户端请求
        bossGroup = new NioEventLoopGroup(1);
        // 工作线程组，处理与客户端的数据通讯，n=2*cpu核心数
        workerGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() * 2);
        // 业务线程，处理业务逻辑，n=cpu核心数
        serviceHandlerGroup = new DefaultEventLoopGroup(Runtime.getRuntime().availableProcessors());
        start();
    }

    @Override
    public void registerService(@Nonnull RpcServiceConfig<?> rpcServiceConfig) throws Exception {
        providerService.publishService(rpcServiceConfig, address);
    }

    private void start() {
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
                        // 使用LengthFieldBasedFrameDecoder解决TCP粘包、粘包问题
                        // 定义每个报文的头4个字节表示消息体长度
                        p.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
                        p.addLast(new Encoder());
                        p.addLast(new Decoder());
                        p.addLast(serviceHandlerGroup, new NettyRpcServerHandler(providerService));
                    }
                });

        // 绑定端口，同步等待
        try {
            serverChannel = serverBootstrap.bind(address).sync().channel();
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
        LOGGER.info("Server is started, listen at [{}]", address);

        // 添加关闭钩子，在程序退出时调用 destroy() 释放资源
        ShutdownHook.getInstance().addDisposable(this);
    }

    @Override
    public void destroy() {
        try {
            providerService.close();
            serverChannel.close();
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            serviceHandlerGroup.shutdownGracefully();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }
}
