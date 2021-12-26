package github.pancras.serialize.javaserial;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.annotation.Nonnull;

import github.pancras.serialize.Serializer;

/**
 * Java原生序列化
 *
 * @author PancrasL
 */
public class JavaSerializer implements Serializer {
    @Override
    public byte[] serialize(@Nonnull Object obj) throws IOException {
        System.out.println("java serial");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(obj);
        return bos.toByteArray();
    }

    @Override
    public <T> T deserialize(@Nonnull byte[] bytes, @Nonnull Class<T> clazz) throws IOException {
        System.out.println("java deserial");
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bis);
        Object obj;
        try {
            obj = ois.readObject();
        } catch (ClassNotFoundException e) {
            throw new IOException(e);
        }
        return (T) obj;
    }
}
