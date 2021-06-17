package github.pancras.remoting.transport.netty.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import github.pancras.commons.factory.SingletonFactory;
import github.pancras.remoting.constants.RpcConstants;
import github.pancras.remoting.dto.RpcMessage;
import github.pancras.remoting.dto.RpcRequest;
import github.pancras.remoting.dto.RpcResponse;
import github.pancras.remoting.invoker.RpcInvoker;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 * @author pancras
 * @create 2021/6/15 19:23
 */
public class NettyRpcServerHandler extends ChannelInboundHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(NettyRpcServerHandler.class);
    private final RpcInvoker rpcInvoker;

    public NettyRpcServerHandler() {
        rpcInvoker = SingletonFactory.getInstance(RpcInvoker.class);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof RpcMessage) {
            RpcMessage rpcMessage = (RpcMessage) msg;
            LOGGER.info("Server receive msg: [{}]", rpcMessage);
            if (rpcMessage.getMessageType() == RpcConstants.REQUEST_TYPE) {
                RpcRequest rpcRequest = (RpcRequest) rpcMessage.getData();
                Object result = rpcInvoker.handle(rpcRequest);

                RpcResponse<Object> rpcResponse;
                if (ctx.channel().isActive() && ctx.channel().isWritable()) {
                    rpcResponse = RpcResponse.success(result, rpcRequest.getRequestId());
                } else {
                    rpcResponse = RpcResponse.fail();
                }
                // 将RpcResponse包装成RpcMessage
                RpcMessage response = new RpcMessage();
                response.setMessageType(RpcConstants.RESPONSE_TYPE);
                response.setData(rpcResponse);
                ctx.writeAndFlush(response);
            }
        }
        ReferenceCountUtil.release(msg);
    }
}