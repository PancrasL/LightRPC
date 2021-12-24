package github.pancras.remoting.transport.netty.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;

import github.pancras.commons.exception.RpcException;
import github.pancras.provider.ProviderService;
import github.pancras.remoting.dto.RpcMessage;
import github.pancras.remoting.dto.RpcRequest;
import github.pancras.remoting.dto.RpcResponse;
import github.pancras.remoting.handler.RpcRequestHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 * @author PancrasL
 */
public class NettyRpcServerHandler extends ChannelInboundHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(NettyRpcServerHandler.class);
    private final RpcRequestHandler rpcRequestHandler;

    public NettyRpcServerHandler(ProviderService providerService) {
        rpcRequestHandler = RpcRequestHandler.newInstance(providerService);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof RpcMessage) {
            RpcMessage rpcMessage = (RpcMessage) msg;
            LOGGER.info("Channel [{}] handle RpcMessage: [{}]", ctx.channel().id().toString(), msg);
            if (rpcMessage.isRequest()) {
                RpcRequest rpcRequest = (RpcRequest) rpcMessage.getData();
                Object result;
                try {
                    result = rpcRequestHandler.handle(rpcRequest);
                } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
                    throw new RpcException(e.getMessage(), e);
                }

                RpcResponse<Object> rpcResponse;
                if (ctx.channel().isActive() && ctx.channel().isWritable()) {
                    rpcResponse = RpcResponse.success(result, rpcRequest.getRequestId());
                } else {
                    rpcResponse = RpcResponse.fail();
                }
                // 将RpcResponse包装成RpcMessage
                RpcMessage response = RpcMessage.newResponse(rpcResponse);
                ctx.writeAndFlush(response);
            }
        }
        ReferenceCountUtil.release(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        //在捕获异常的时候调用，发生异常并且如果通道处于激活状态就关闭
        Channel channel = ctx.channel();
        if (channel.isActive()) {
            LOGGER.error("The remote host [{}] has closed the connection.", channel.remoteAddress());
            ctx.close();
            cause.printStackTrace();
        }
    }
}
