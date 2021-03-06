package github.pancras.registry;

/**
 * @author PancrasL
 */
enum RegistryType {
    /**
     * Zookeeper registry type.
     */
    Zookeeper,

    /**
     * Redis registry type.
     */
    Redis;

    static RegistryType getType(String name) {
        for (RegistryType registryType : RegistryType.values()) {
            if (registryType.name().equalsIgnoreCase(name)) {
                return registryType;
            }
        }
        throw new IllegalArgumentException("Not support registry type: " + name);
    }
}
