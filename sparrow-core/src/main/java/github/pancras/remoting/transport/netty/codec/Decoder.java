package github.pancras.remoting.transport.netty.codec;

import java.util.List;

import github.pancras.serialize.Serializer;
import github.pancras.serialize.kryo.KryoSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * @author pancras
 * @create 2021/6/16 20:48
 */
public class Decoder extends ByteToMessageDecoder {
    // 消息的头4个字节表示消息体长度
    private static final int BODY_LENGTH = 4;
    private final Serializer serializer = new KryoSerializer();
    private final Class clazz;

    public Decoder(Class clazz) {
        this.clazz = clazz;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() >= BODY_LENGTH) {
            in.markReaderIndex();
            int dataLength = in.readInt();
            byte[] body = new byte[dataLength];
            in.readBytes(body);
            Object obj = serializer.deserialize(body, clazz);
            out.add(obj);
        }
    }
}
