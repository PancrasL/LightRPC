package github.pancras.registry;

import github.pancras.config.DefaultConfig;
import github.pancras.registry.redis.RedisRegistryServiceImpl;
import github.pancras.registry.zk.ZkRegistryServiceImpl;

/**
 * @author PancrasL
 */
public class RegistryFactory {
    private static volatile RegistryService INSTANCE = null;

    private RegistryFactory() {
    }

    public static RegistryService getInstance() {
        if (INSTANCE == null) {
            synchronized (RegistryFactory.class) {
                if (INSTANCE == null) {
                    INSTANCE = buildRegistryService();
                }
            }
        }
        return INSTANCE;
    }

    private static RegistryService buildRegistryService() {
        RegistryType registryType;
        String type = DefaultConfig.DEFAULT_REGISRY_TYPE;
        registryType = RegistryType.getType(type);

        switch (registryType) {
            case Zookeeper:
                INSTANCE = new ZkRegistryServiceImpl();
                break;
            case Redis:
                INSTANCE = new RedisRegistryServiceImpl();
                break;
            default:
                throw new IllegalArgumentException("Unknown type：" + registryType);
        }
        return INSTANCE;
    }

}
