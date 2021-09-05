package github.pancras.serialize;

import java.io.IOException;

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
    byte[] serialize(Object obj) throws IOException;

    /**
     * byte[] -> obj
     *
     * @param bytes byte[]
     * @param clazz 反序列化类型
     * @param <T>   类型
     * @return obj
     * @throws IOException 反序列化异常
     */
    <T> T deserialize(byte[] bytes, Class<T> clazz) throws IOException;
}
