package github.pancras.serialize;

/**
 * @author PancrasL
 */
enum SerializerType {
    /**
     * Java serializer type.
     */
    Java,

    /**
     * Zookeeper serializer type.
     */
    Kryo,

    /**
     * Redis serializer type.
     */
    Protostuff;

    static SerializerType getType(String name) {
        for (SerializerType registryType : SerializerType.values()) {
            if (registryType.name().equalsIgnoreCase(name)) {
                return registryType;
            }
        }
        throw new IllegalArgumentException("Not support serializer type: " + name);
    }
}
