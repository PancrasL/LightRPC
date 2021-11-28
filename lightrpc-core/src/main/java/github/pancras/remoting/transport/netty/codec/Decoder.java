package github.pancras.remoting.transport.netty.codec;

import java.util.List;

import github.pancras.remoting.dto.RpcMessage;
import github.pancras.serialize.Serializer;
import github.pancras.serialize.SerializerFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * @author PancrasL
 */
public class Decoder extends ByteToMessageDecoder {
    private final Serializer serializer = SerializerFactory.getInstance();

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int len = in.readableBytes();
        byte[] body = new byte[len];
        in.readBytes(body);
        Object obj = serializer.deserialize(body, RpcMessage.class);
        out.add(obj);
    }
}
