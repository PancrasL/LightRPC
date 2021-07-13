package github.pancras.registry;

import java.io.Closeable;
import java.net.InetSocketAddress;

/**
 * @author pancras
 * @create 2021/6/9 18:06
 */
public interface ServiceDiscovery extends Closeable {
    /**
     * 从注册中心中查找服务
     *
     * @param rpcServiceName RPC服务名称
     * @return 服务地址
     */
    InetSocketAddress lookupService(String rpcServiceName);
}
