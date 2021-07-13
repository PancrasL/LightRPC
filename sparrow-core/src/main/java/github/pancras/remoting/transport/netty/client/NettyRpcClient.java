package github.pancras.remoting.transport.netty.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;

import github.pancras.commons.factory.DiscoveryFactory;
import github.pancras.config.SparrowConfig;
import github.pancras.registry.ServiceDiscovery;
import github.pancras.remoting.constants.RpcConstants;
import github.pancras.remoting.dto.RpcMessage;
import github.pancras.remoting.dto.RpcRequest;
import github.pancras.remoting.dto.RpcResponse;
import github.pancras.remoting.transport.RpcClient;
import github.pancras.remoting.transport.netty.codec.Decoder;
import github.pancras.remoting.transport.netty.codec.Encoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @author pancras
 * @create 2021/6/16 10:22
 */
public class NettyRpcClient implements RpcClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(NettyRpcClient.class);

    private final ServiceDiscovery serviceDiscovery;
    private final Bootstrap bootstrap;
    private final EventLoopGroup workerGroup;
    private final ChannelPool channelPool;
    private final UnprocessedRequests unprocessedRequests;

    public NettyRpcClient() {
        bootstrap = new Bootstrap();
        // 处理与服务端通信的线程组
        workerGroup = new NioEventLoopGroup();
        bootstrap.group(workerGroup)
                .channel(NioSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, SparrowConfig.CONNECT_TIMEOUT_MILLIS)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ChannelPipeline p = ch.pipeline();
                        p.addLast(new Decoder());
                        p.addLast(new Encoder());
                        p.addLast(new NettyRpcClientHandler(unprocessedRequests));
                    }
                });
        serviceDiscovery = DiscoveryFactory.getDiscovery(SparrowConfig.DEFAULT_REGISRY_TYPE);
        channelPool = new ChannelPool();
        unprocessedRequests = new UnprocessedRequests();
    }

    @Override
    public Object sendRpcRequest(RpcRequest rpcRequest) throws Exception {
        CompletableFuture<RpcResponse<Object>> resultFuture = new CompletableFuture<>();
        InetSocketAddress inetSocketAddress = serviceDiscovery.lookupService(rpcRequest.getRpcServiceName());

        // 从ChannelPool中获取和服务器连接的Channel，避免重复连接
        Channel channel = getChannel(inetSocketAddress);
        if (channel.isActive()) {
            unprocessedRequests.put(rpcRequest.getRequestId(), resultFuture);

            RpcMessage rpcMessage = new RpcMessage();
            rpcMessage.setMessageType(RpcConstants.REQUEST_TYPE);
            rpcMessage.setData(rpcRequest);
            channel.writeAndFlush(rpcMessage).addListener(future -> {
                if (future.isSuccess()) {
                    LOGGER.debug("Client send message: [{}]", rpcMessage);
                } else {
                    LOGGER.error("Client send failed");
                }
            });
        }

        return resultFuture.get();
    }

    private Channel getChannel(InetSocketAddress inetSocketAddress) throws InterruptedException {
        Channel channel = channelPool.getOrNull(inetSocketAddress);
        if (channel == null) {
            channel = doConnect(inetSocketAddress);
            channelPool.addChannel(inetSocketAddress, channel);
        }
        return channel;
    }

    private Channel doConnect(InetSocketAddress inetSocketAddress) throws InterruptedException {
        ChannelFuture future = bootstrap.connect(inetSocketAddress).sync();
        LOGGER.info("Connect to server [{}] success", inetSocketAddress.toString());
        return future.channel();
    }

    @Override
    public void close() {
        try {
            serviceDiscovery.close();
        } catch (IOException ignored) {
        }
        channelPool.close();
        workerGroup.shutdownGracefully();
    }
}
