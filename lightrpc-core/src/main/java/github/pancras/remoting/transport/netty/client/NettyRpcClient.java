package github.pancras.remoting.transport.netty.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Nonnull;

import github.pancras.commons.ShutdownHook;
import github.pancras.discover.DiscoverService;
import github.pancras.discover.DiscoverServiceImpl;
import github.pancras.registry.RegistryFactory;
import github.pancras.remoting.dto.RpcMessage;
import github.pancras.remoting.dto.RpcRequest;
import github.pancras.remoting.dto.RpcResponse;
import github.pancras.remoting.transport.RpcClient;
import github.pancras.remoting.transport.netty.codec.Decoder;
import github.pancras.remoting.transport.netty.codec.Encoder;
import github.pancras.wrapper.RegistryConfig;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.pool.AbstractChannelPoolMap;
import io.netty.channel.pool.ChannelPoolHandler;
import io.netty.channel.pool.ChannelPoolMap;
import io.netty.channel.pool.FixedChannelPool;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;

/**
 * @author PancrasL
 */
public class NettyRpcClient implements RpcClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(NettyRpcClient.class);

    private final DiscoverService discoverService;
    private final Bootstrap bootstrap;
    private final EventLoopGroup workerGroup;
    private final Set<InetSocketAddress> CONNECTED_ADDRESS = ConcurrentHashMap.newKeySet();
    private final ChannelPoolMap<InetSocketAddress, FixedChannelPool> poolMap;
    private final UnprocessedRequests unprocessedRequests;

    private NettyRpcClient(RegistryConfig registryConfig) {
        bootstrap = new Bootstrap();
        // 处理与服务端通信的线程组
        workerGroup = new NioEventLoopGroup();
        bootstrap.group(workerGroup)
                .channel(NioSocketChannel.class);

        discoverService = DiscoverServiceImpl.getInstance(RegistryFactory.getRegistry(registryConfig));
        unprocessedRequests = new UnprocessedRequests();
        poolMap = createPoolMap();

        ShutdownHook.getInstance().addDisposable(this);
    }

    public static NettyRpcClient getInstance(RegistryConfig registryConfig) {
        return new NettyRpcClient(registryConfig);
    }

    private ChannelPoolMap<InetSocketAddress, FixedChannelPool> createPoolMap() {
        ChannelPoolMap<InetSocketAddress, FixedChannelPool> poolMap;
        poolMap = new AbstractChannelPoolMap<InetSocketAddress, FixedChannelPool>() {
            @Override
            protected FixedChannelPool newPool(InetSocketAddress socketAddress) {
                ChannelPoolHandler handler = new ChannelPoolHandler() {
                    // 调用released会触发
                    @Override
                    public void channelReleased(Channel ch) {
                        LOGGER.debug(String.format("Channel %s released", ch));
                    }

                    // 当channel不足时会触发，但不会超过最大channel数
                    @Override
                    public void channelAcquired(Channel ch) {
                        LOGGER.debug(String.format("Channel %s acquired", ch));
                    }

                    // 获取连接池中的chanel
                    @Override
                    public void channelCreated(Channel ch) {
                        ChannelPipeline p = ch.pipeline();
                        // 使用LengthFieldBasedFrameDecoder解决TCP粘包、粘包问题
                        p.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
                        p.addLast(new Encoder());
                        p.addLast(new Decoder());
                        p.addLast(new NettyRpcClientHandler(unprocessedRequests));
                        LOGGER.debug(String.format("Channel %s created", ch));
                    }
                };
                return new FixedChannelPool(bootstrap.remoteAddress(socketAddress), handler, 5);
            }
        };
        return poolMap;
    }

    @Override
    public Object sendRpcRequest(@Nonnull RpcRequest rpcRequest) throws Exception {
        InetSocketAddress socketAddress = discoverService.lookup(rpcRequest.getRpcServiceName());

        // 从ChannelPool中获取和服务器连接的Channel，避免重复连接
        FixedChannelPool pool = poolMap.get(socketAddress);
        Future<Channel> channel = pool.acquire();
        RpcMessage rpcMessage = RpcMessage.newRequest(rpcRequest);
        CompletableFuture<RpcResponse<?>> result = new CompletableFuture<>();
        channel.addListener(new FutureListener<Channel>() {
            @Override
            public void operationComplete(Future<Channel> future) {
                if (future.isSuccess()) {
                    unprocessedRequests.put(rpcRequest.getRequestId(), result);
                    Channel ch = future.getNow();
                    ch.writeAndFlush(rpcMessage);
                    pool.release(ch);
                }
            }
        });

        return result.get();
    }

    @Override
    public void destroy() {
        workerGroup.shutdownGracefully();
        for (InetSocketAddress address : CONNECTED_ADDRESS) {
            if (poolMap.contains(address)) {
                poolMap.get(address).closeAsync();
            }
        }
        discoverService.close();
    }
}
