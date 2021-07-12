package github.pancras.registry;

import java.io.Closeable;
import java.net.InetSocketAddress;

/**
 * @author pancras
 * @create 2021/6/9 18:06
 */
public interface ServiceDiscovery extends Closeable {
    InetSocketAddress lookupService(String rpcServiceName);
}
