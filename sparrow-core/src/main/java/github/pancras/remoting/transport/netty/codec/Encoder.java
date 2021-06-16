package github.pancras.remoting.transport.netty.codec;

import github.pancras.serialize.Serializer;
import github.pancras.serialize.kryo.KryoSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author pancras
 * @create 2021/6/16 20:48
 */
public class Encoder extends MessageToByteEncoder<Object> {
    private final Serializer serializer = new KryoSerializer();

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        byte[] body = serializer.serialize(msg);
        int dataLength = body.length;
        out.writeInt(dataLength);
        out.writeBytes(body);
    }
}
