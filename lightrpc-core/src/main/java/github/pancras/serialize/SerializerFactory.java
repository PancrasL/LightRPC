package github.pancras.serialize;

import github.pancras.config.DefaultConfig;
import github.pancras.serialize.javaserial.JavaSerializer;
import github.pancras.serialize.kryo.KryoSerializer;
import github.pancras.serialize.protostuff.ProtostuffSerializer;

/**
 * @author PancrasL
 */
public class SerializerFactory {
    private SerializerFactory() {
    }

    public static Serializer getInstance() {
        String name = DefaultConfig.SERIALIZER_TYPE;
        SerializerType type = SerializerType.getType(name);
        switch (type) {
            case Java:
                return new JavaSerializer();
            case Kryo:
                return new KryoSerializer();
            case Protostuff:
                return new ProtostuffSerializer();
            default:
                throw new IllegalArgumentException("Unknown type: " + type);
        }
    }
}
