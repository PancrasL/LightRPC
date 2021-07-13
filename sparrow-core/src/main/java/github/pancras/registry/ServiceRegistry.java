package github.pancras.registry;

import java.io.Closeable;
import java.net.InetSocketAddress;

/**
 * @author pancras
 * @create 2021/6/9 18:06
 */
public interface ServiceRegistry extends Closeable {
    /**
     * 将RPC服务注册到注册中心
     *
     * @param rpcServiceName    RPC服务名称
     * @param inetSocketAddress RPC服务地址
     */
    void registerService(String rpcServiceName, InetSocketAddress inetSocketAddress);
}