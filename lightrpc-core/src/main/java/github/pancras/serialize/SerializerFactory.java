package github.pancras.serialize;

import github.pancras.commons.enums.SerializerType;
import github.pancras.config.DefaultConfig;
import github.pancras.serialize.kryo.KryoSerializer;
import github.pancras.serialize.protostuff.ProtostuffSerializer;

/**
 * @author PancrasL
 */
public class SerializerFactory {
    private SerializerFactory() {

    }

    public static Serializer getInstance() {
        String name = DefaultConfig.DEFAULT_SERIALIZER_TYPE;
        SerializerType type = SerializerType.getType(name);
        switch (type) {
            case Kryo:
                return new KryoSerializer();
            case Protostuff:
                return new ProtostuffSerializer();
            default:
                throw new IllegalArgumentException("Unknown type: " + type);
        }
    }
}
