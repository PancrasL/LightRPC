package github.pancras.registry;

import java.net.InetSocketAddress;

/**
 * @author pancras
 * @create 2021/6/9 18:06
 */
public interface ServiceDiscovery {
    InetSocketAddress lookupService(String rpcServiceName);
}
