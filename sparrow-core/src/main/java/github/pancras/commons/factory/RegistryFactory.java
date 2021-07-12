package github.pancras.commons.factory;

import github.pancras.config.Constant;
import github.pancras.registry.ServiceRegistry;
import github.pancras.registry.redis.RedisServiceRegistryImpl;
import github.pancras.registry.zk.ZkServiceRegistryImpl;

/**
 * @author PancrasL
 */
public class RegistryFactory {
    private RegistryFactory() {

    }

    public static ServiceRegistry getRegistry(String type) {
        switch (type) {
            case Constant.ZOOKEEPER:
                return new ZkServiceRegistryImpl();
            case Constant.REDIS:
                return new RedisServiceRegistryImpl();
            default:
                throw new IllegalArgumentException("Unsupported registry type.");
        }
    }
}
