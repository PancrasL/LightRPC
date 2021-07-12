package github.pancras.commons.factory;

import github.pancras.config.Constant;
import github.pancras.registry.ServiceDiscovery;
import github.pancras.registry.redis.RedisServiceDiscoveryImpl;
import github.pancras.registry.zk.ZkServiceDiscoveryImpl;

/**
 * @author PancrasL
 */
public class DiscoveryFactory {
    private DiscoveryFactory() {

    }

    public static ServiceDiscovery getDiscovery(String type) {
        switch (type) {
            case Constant.ZOOKEEPER:
                return new ZkServiceDiscoveryImpl();
            case Constant.REDIS:
                return new RedisServiceDiscoveryImpl();
            default:
                throw new IllegalArgumentException("Unsupported registry type.");
        }
    }
}
