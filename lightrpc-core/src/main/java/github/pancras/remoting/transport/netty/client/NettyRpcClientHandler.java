package github.pancras.remoting.transport.netty.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import github.pancras.commons.enums.MessageType;
import github.pancras.remoting.dto.RpcMessage;
import github.pancras.remoting.dto.RpcResponse;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 * @author PancrasL
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
                if (rpcMessage.getMessageType() == MessageType.RpcResponse) {
                    unprocessedRequests.complete((RpcResponse<Object>) rpcMessage.getData());
                }
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        //在捕获异常的时候调用，发生异常并且如果通道处于激活状态就关闭
        Channel channel = ctx.channel();
        if (channel.isActive()) {
            LOGGER.warn("The remote server [{}] has closed the connection.", ctx.channel().remoteAddress());
            ctx.close();
        }
    }
}
