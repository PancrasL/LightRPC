package github.pancras.serialize.javaserial;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class JavaSerializerTest {
    @Test
    public void testString() throws IOException {
        String s = "abcd";
        JavaSerializer serializer = new JavaSerializer();
        byte[] bytes = serializer.serialize(s);
        String ds = serializer.deserialize(bytes, String.class);
        assertEquals(s, ds);
    }
}