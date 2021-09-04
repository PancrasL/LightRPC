package github.pancras.remoting.transport.netty.codec;

import github.pancras.commons.factory.SerializerFactory;
import github.pancras.config.SparrowConfig;
import github.pancras.serialize.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author PancrasL
 */
public class Encoder extends MessageToByteEncoder<Object> {
    private final Serializer serializer = SerializerFactory.getSerializer(SparrowConfig.DEFAULT_SERIALIZER_TYPE);

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        byte[] body = serializer.serialize(msg);
        int dataLength = body.length;
        out.writeInt(dataLength);
        out.writeBytes(body);
    }
}
