package github.pancras.serialize.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import github.pancras.remoting.dto.RpcRequest;
import github.pancras.remoting.dto.RpcResponse;
import github.pancras.serialize.Serializer;

/**
 * @author pancras
 * @create 2021/6/10 17:23
 */
public class KryoSerializer implements Serializer {
    /**
     * Kryo不是线程安全的，利用ThreadLocal为每个线程创建自己的Kryo
     */
    private static final ThreadLocal<Kryo> kryoThreadLocal = new ThreadLocal<Kryo>() {
        @Override
        protected Kryo initialValue() {
            Kryo kryo = new Kryo();
            kryo.register(RpcRequest.class);
            kryo.register(RpcResponse.class);
            return kryo;
        }
    };

    @Override
    public byte[] serialize(Object obj) throws IOException {
        try (Output output = new Output(new ByteArrayOutputStream())) {
            Kryo kryo = kryoThreadLocal.get();
            // obj --> byte[]
            kryo.writeObject(output, obj);
            kryoThreadLocal.remove();
            return output.toBytes();
        } catch (KryoException e) {
            throw new IOException(e);
        }
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) throws IOException {
        try (Input input = new Input(new ByteArrayInputStream(bytes))) {
            Kryo kryo = kryoThreadLocal.get();
            // byte[] --> obj
            Object obj = kryo.readObject(input, clazz);
            kryoThreadLocal.remove();
            return clazz.cast(obj);
        } catch (KryoException e) {
            throw new IOException(e);
        }
    }
}
