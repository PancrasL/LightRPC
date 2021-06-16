package github.pancras.remoting.transport.netty.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import github.pancras.factory.SingletonFactory;
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
public class NettyRpcRequestHandler extends ChannelInboundHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(NettyRpcRequestHandler.class);
    private final RpcInvoker rpcInvoker;

    public NettyRpcRequestHandler() {
        rpcInvoker = SingletonFactory.getInstance(RpcInvoker.class);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof RpcRequest) {
            RpcRequest rpcRequest = (RpcRequest) msg;
            LOGGER.info("Server receive msg: [{}]", rpcRequest);
            RpcResponse rpcResponse;
            Object result = rpcInvoker.handle(rpcRequest);
            if (ctx.channel().isActive() && ctx.channel().isWritable()) {
                rpcResponse = RpcResponse.success(result, rpcRequest.getRequestId());
            } else {
                rpcResponse = RpcResponse.fail();
            }
            ctx.writeAndFlush(rpcResponse);
        }
        ReferenceCountUtil.release(msg);
    }
}
