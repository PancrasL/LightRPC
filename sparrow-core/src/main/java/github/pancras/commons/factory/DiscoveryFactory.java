package github.pancras.commons.factory;

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
            case "zookeeper":
                return new ZkServiceDiscoveryImpl();
            case "redis":
                return new RedisServiceDiscoveryImpl();
            default:
                return null;
        }
    }
}
