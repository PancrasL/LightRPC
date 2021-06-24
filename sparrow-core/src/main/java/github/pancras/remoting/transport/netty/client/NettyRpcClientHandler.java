package github.pancras.remoting.transport.netty.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import github.pancras.remoting.constants.RpcConstants;
import github.pancras.remoting.dto.RpcMessage;
import github.pancras.remoting.dto.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 * @author pancras
 * @create 2021/6/16 11:02
 */
public class NettyRpcClientHandler extends ChannelInboundHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(NettyRpcClientHandler.class);
    private final UnprocessedRequests unprocessedRequests;

    public NettyRpcClientHandler(UnprocessedRequests unprocessedRequests) {
        this.unprocessedRequests = unprocessedRequests;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            if (msg instanceof RpcMessage) {
                RpcMessage rpcMessage = (RpcMessage) msg;
                LOGGER.info("Client channel [{}] receive msg: [{}]", ctx.channel().id().toString(), rpcMessage);
                if (rpcMessage.getMessageType() == RpcConstants.RESPONSE_TYPE) {
                    unprocessedRequests.complete((RpcResponse<Object>) rpcMessage.getData());
                }
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }
}
