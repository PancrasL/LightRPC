package github.pancras.registry;

import github.pancras.commons.enums.RegistryType;
import github.pancras.config.DefaultConfig;
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
        String type = DefaultConfig.DEFAULT_REGISRY_TYPE;
        registryType = RegistryType.getType(type);

        switch (registryType) {
            case Zookeeper:
                instance = new ZkRegistryServiceImpl();
                break;
            case Redis:
                instance = new RedisRegistryServiceImpl();
                break;
            default:
                throw new IllegalArgumentException("Unknown type：" + registryType);
        }
        return instance;
    }

}
