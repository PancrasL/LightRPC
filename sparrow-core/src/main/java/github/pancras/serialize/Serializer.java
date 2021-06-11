package github.pancras.serialize;

import java.io.IOException;

/**
 * @author pancras
 * @create 2021/6/10 17:21
 */
public interface Serializer {
    byte[] serialize(Object obj) throws IOException;

    <T> T deserialize(byte[] bytes, Class<T> clazz) throws IOException;
}
