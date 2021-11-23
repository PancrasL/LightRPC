package github.pancras.registry;

import java.net.InetSocketAddress;

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
        String type = DefaultConfig.DEFAULT_REGISRY_TYPE;
        RegistryType registryType = RegistryType.getType(type);

        switch (registryType) {
            case Zookeeper:
                INSTANCE = ZkRegistryServiceImpl.newInstance(
                        InetSocketAddress.createUnresolved(
                                DefaultConfig.DEFAULT_ZK_ADDRESS, DefaultConfig.DEFAULT_ZK_PORT));
                break;
            case Redis:
                INSTANCE = RedisRegistryServiceImpl.newInstance(
                        InetSocketAddress.createUnresolved(
                                DefaultConfig.DEFAULT_REDIS_ADDRESS, DefaultConfig.DEFAULT_REDIS_PORT));
                break;
            default:
                throw new IllegalArgumentException("Unknown type：" + registryType);
        }
        return INSTANCE;
    }

}
