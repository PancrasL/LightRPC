package github.pancras.commons.factory;

import github.pancras.config.constant.Constant;
import github.pancras.serialize.Serializer;
import github.pancras.serialize.kryo.KryoSerializer;
import github.pancras.serialize.protostuff.ProtostuffSerializer;

/**
 * @author PancrasL
 */
public class SerializerFactory {
    private SerializerFactory() {

    }

    public static Serializer getSerializer(String type) {
        switch (type) {
            case Constant.KRYO:
                return new KryoSerializer();
            case Constant.PROTOSTUFF:
                return new ProtostuffSerializer();
            default:
                throw new IllegalArgumentException("Unsupported serializer type.");
        }
    }
}
