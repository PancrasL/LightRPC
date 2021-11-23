package github.pancras.serialize;

import java.io.IOException;

import javax.annotation.Nonnull;

/**
 * @author PancrasL
 */
public interface Serializer {
    /**
     * obj -> byte[]
     *
     * @param obj 待序列化的对象
     * @return byte[]
     * @throws IOException 序列化异常
     */
    byte[] serialize(@Nonnull Object obj) throws IOException;

    /**
     * byte[] -> obj
     *
     * @param bytes byte[]
     * @param clazz 反序列化类型
     * @param <T>   类型
     * @return obj
     * @throws IOException 反序列化异常
     */
    <T> T deserialize(@Nonnull byte[] bytes, @Nonnull Class<T> clazz) throws IOException;
}
