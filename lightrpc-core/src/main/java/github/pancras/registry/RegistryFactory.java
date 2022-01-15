package github.pancras.registry;

import java.net.InetSocketAddress;

import github.pancras.commons.utils.NetUtil;
import github.pancras.registry.redis.RedisRegistryServiceImpl;
import github.pancras.registry.zk.ZkRegistryServiceImpl;

/**
 * @author PancrasL
 */
public class RegistryFactory {
    private RegistryFactory() {
    }

    public static RegistryService getRegistry(String registryAddress) {
        return buildRegistryService(registryAddress);
    }

    private static RegistryService buildRegistryService(String registryAddress) {
        String[] typeAndAddress = registryAddress.split("://");
        InetSocketAddress socketAddress = NetUtil.toInetSocketAddress(typeAndAddress[1]);

        RegistryType registryType = RegistryType.getType(typeAndAddress[0]);
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
