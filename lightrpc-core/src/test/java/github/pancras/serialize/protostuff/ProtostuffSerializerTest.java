package github.pancras.serialize.protostuff;

import org.junit.Test;

import java.io.IOException;

import github.pancras.serialize.Serializer;

import static org.junit.Assert.assertEquals;

/**
 * @author PancrasL
 */
public class ProtostuffSerializerTest {

    @Test
    public void test() throws IOException {
        String s = "hello, world.";
        Serializer serializer = new ProtostuffSerializer();
        byte[] bytes = serializer.serialize(s);
        String s1 = serializer.deserialize(bytes, String.class);
        assertEquals(s, s1);
    }
}