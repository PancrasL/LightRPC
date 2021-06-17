package github.pancras.remoting.transport.netty.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;

import github.pancras.registry.ServiceDiscovery;
import github.pancras.registry.zk.ZkServiceDiscoveryImpl;
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
import io.netty.util.AttributeKey;

/**
 * @author pancras
 * @create 2021/6/16 10:22
 */
public class NettyRpcClient implements RpcClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(NettyRpcClient.class);

    private final ServiceDiscovery serviceDiscovery;
    private final Bootstrap bootstrap;
    private final EventLoopGroup eventLoopGroup;

    public NettyRpcClient() {
        this.serviceDiscovery = new ZkServiceDiscoveryImpl();
        this.bootstrap = new Bootstrap();
        this.eventLoopGroup = new NioEventLoopGroup();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ChannelPipeline p = ch.pipeline();
                        p.addLast(new Decoder());
                        p.addLast(new Encoder());
                        p.addLast(new NettyRpcClientHandler());
                    }
                });
    }

    @Override
    public Object sendRpcRequest(RpcRequest rpcRequest) throws Exception {
        CompletableFuture<RpcResponse<Object>> resultFuture = new CompletableFuture<>();
        InetSocketAddress inetSocketAddress = serviceDiscovery.lookupService(rpcRequest.getRpcServiceName());

        ChannelFuture f = bootstrap.connect(inetSocketAddress).sync();
        LOGGER.info("Connect to server: [{}]", inetSocketAddress);
        Channel channel = f.channel();
        if (channel != null) {
            RpcMessage rpcMessage = new RpcMessage();
            rpcMessage.setMessageType(RpcConstants.REQUEST_TYPE);
            rpcMessage.setData(rpcRequest);
            channel.writeAndFlush(rpcMessage).addListener(future -> {
                if (future.isSuccess()) {
                    LOGGER.info("Client send message: [{}]", rpcRequest);
                } else {
                    LOGGER.error("Client send failed");
                }
            });
            // 阻塞等待，直到channel关闭
            channel.closeFuture().sync();

            AttributeKey<RpcResponse<Object>> key = AttributeKey.valueOf("rpcResponse");
            return channel.attr(key).get();
        }
        return resultFuture;
    }
}
