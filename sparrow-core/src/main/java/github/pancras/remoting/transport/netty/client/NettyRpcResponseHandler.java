package github.pancras.remoting.transport.netty.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import github.pancras.factory.SingletonFactory;
import github.pancras.remoting.dto.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;
import io.netty.util.ReferenceCountUtil;

/**
 * @author pancras
 * @create 2021/6/16 11:02
 */
public class NettyRpcResponseHandler extends ChannelInboundHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(NettyRpcResponseHandler.class);
    private final NettyRpcClient nettyRpcClient;

    public NettyRpcResponseHandler() {
        this.nettyRpcClient = SingletonFactory.getInstance(NettyRpcClient.class);
    }

    /**
     * Read the message transmitted by the server
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            if (msg instanceof RpcResponse) {
                RpcResponse rpcResponse = (RpcResponse) msg;
                LOGGER.info("Client receive msg: [{}]", rpcResponse);

                AttributeKey<RpcResponse> key = AttributeKey.valueOf("rpcResponse");
                ctx.channel().attr(key).set(rpcResponse);
                ctx.channel().close();
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }
}
