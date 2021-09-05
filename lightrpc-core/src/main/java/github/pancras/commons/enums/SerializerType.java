package github.pancras.commons.enums;

/**
 * @author PancrasL
 */
public enum SerializerType {
    /**
     * Zookeeper registry type.
     */
    Kryo,

    /**
     * Redis registry type.
     */
    Protostuff;

    public static SerializerType getType(String name) {
        for (SerializerType registryType : SerializerType.values()) {
            if (registryType.name().equalsIgnoreCase(name)) {
                return registryType;
            }
        }
        throw new IllegalArgumentException("Not support serializer type: " + name);
    }
}
