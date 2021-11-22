package github.pancras.serialize.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.annotation.Nonnull;

import github.pancras.remoting.dto.RpcRequest;
import github.pancras.remoting.dto.RpcResponse;
import github.pancras.serialize.Serializer;

/**
 * @author PancrasL
 */
public class KryoSerializer implements Serializer {
    /**
     * Kryo不是线程安全的，利用ThreadLocal为每个线程创建自己的Kryo
     */
    private static final ThreadLocal<Kryo> KRYO_THREAD_LOCAL = new ThreadLocal<Kryo>() {
        @Override
        protected Kryo initialValue() {
            Kryo kryo = new Kryo();
            kryo.register(RpcRequest.class);
            kryo.register(RpcResponse.class);
            return kryo;
        }
    };

    @Override
    public byte[] serialize(@Nonnull Object obj) throws IOException {
        try (Output output = new Output(new ByteArrayOutputStream())) {
            Kryo kryo = KRYO_THREAD_LOCAL.get();
            // obj --> byte[]
            kryo.writeObject(output, obj);
            KRYO_THREAD_LOCAL.remove();
            return output.toBytes();
        } catch (KryoException e) {
            throw new IOException(e);
        }
    }

    @Override
    public <T> T deserialize(@Nonnull byte[] bytes, @Nonnull Class<T> clazz) throws IOException {
        try (Input input = new Input(new ByteArrayInputStream(bytes))) {
            Kryo kryo = KRYO_THREAD_LOCAL.get();
            // byte[] --> obj
            Object obj = kryo.readObject(input, clazz);
            KRYO_THREAD_LOCAL.remove();
            return clazz.cast(obj);
        } catch (KryoException e) {
            throw new IOException(e);
        }
    }
}
