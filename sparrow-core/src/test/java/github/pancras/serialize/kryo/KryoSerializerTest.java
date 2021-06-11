package github.pancras.serialize.kryo;

import org.junit.Test;

import java.io.IOException;

import github.pancras.serialize.Serializer;

import static org.junit.Assert.assertEquals;

/**
 * @author pancras
 * @create 2021/6/11 11:23
 */
public class KryoSerializerTest {

    @Test
    public void testString() throws IOException {
        String msg = "Good morning!";
        Serializer serializer = new KryoSerializer();
        byte[] bytes = serializer.serialize(msg);
        String msg1 = serializer.deserialize(bytes, String.class);
        assertEquals(msg, msg1);
    }
}