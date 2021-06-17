package github.pancras.remoting.transport.netty.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import github.pancras.remoting.constants.RpcConstants;
import github.pancras.remoting.dto.RpcMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;
import io.netty.util.ReferenceCountUtil;

/**
 * @author pancras
 * @create 2021/6/16 11:02
 */
public class NettyRpcClientHandler extends ChannelInboundHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(NettyRpcClientHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof RpcMessage) {
            RpcMessage rpcMessage = (RpcMessage) msg;
            LOGGER.info("Client receive msg: [{}]", rpcMessage);
            if (rpcMessage.getMessageType() == RpcConstants.RESPONSE_TYPE) {
                AttributeKey<Object> key = AttributeKey.valueOf("rpcResponse");
                ctx.channel().attr(key).set(rpcMessage.getData());
                ctx.channel().close();
            }
        }
        ReferenceCountUtil.release(msg);
    }
}
