package github.pancras.registry;

import java.net.InetSocketAddress;

import github.pancras.commons.utils.NetUtil;
import github.pancras.registry.redis.RedisRegistryServiceImpl;
import github.pancras.registry.zk.ZkRegistryServiceImpl;
import github.pancras.wrapper.RegistryConfig;

/**
 * @author PancrasL
 */
public class RegistryFactory {
    private RegistryFactory() {
    }

    public static RegistryService getRegistry(RegistryConfig registryConfig) {
        return buildRegistryService(registryConfig);
    }

    private static RegistryService buildRegistryService(RegistryConfig registryConfig) {
        InetSocketAddress socketAddress = new InetSocketAddress(registryConfig.getHost(), registryConfig.getPort());
        NetUtil.validAddress(socketAddress);

        String type = registryConfig.getType();
        RegistryType registryType = RegistryType.getType(type);
        RegistryService registryService;
        switch (registryType) {
            case Zookeeper:
                registryService = ZkRegistryServiceImpl.newInstance(socketAddress);
                break;
            case Redis:
                registryService = RedisRegistryServiceImpl.newInstance(socketAddress);
                break;
            default:
                throw new IllegalArgumentException("Unknown type：" + registryType);
        }
        return registryService;
    }

}
