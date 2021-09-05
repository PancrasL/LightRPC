package github.pancras.serialize.kryo;

import org.junit.Test;

import java.io.IOException;

import github.pancras.serialize.Serializer;

import static org.junit.Assert.assertEquals;

/**
 * @author pancras
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