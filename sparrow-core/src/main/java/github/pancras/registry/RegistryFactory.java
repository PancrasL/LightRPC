package github.pancras.registry;

import github.pancras.config.SparrowConfig;
import github.pancras.registry.redis.RedisRegistryServiceImpl;
import github.pancras.registry.zk.ZkRegistryServiceImpl;

/**
 * @author PancrasL
 */
public class RegistryFactory {
    private static volatile RegistryService instance = null;

    private RegistryFactory() {
    }

    public static RegistryService getInstance() {
        if (instance == null) {
            synchronized (RegistryFactory.class) {
                if (instance == null) {
                    instance = buildRegistryService();
                }
            }
        }
        return instance;
    }

    private static RegistryService buildRegistryService() {
        RegistryType registryType;
        String type = SparrowConfig.DEFAULT_REGISRY_TYPE;
        try {
            registryType = RegistryType.getType(type);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Not support registry type: " + type);
        }
        switch (registryType) {
            case Zookeeper:
                instance = new ZkRegistryServiceImpl();
                break;
            case Redis:
                instance = new RedisRegistryServiceImpl();
                break;
            default:
                throw new IllegalArgumentException("Unsupported registry type.");
        }
        return instance;
    }

}
