package github.pancras.registry;

import java.net.InetSocketAddress;

import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;

/**
 * @author PancrasL
 */
@ThreadSafe
public interface RegistryService {
    /**
     * server调用，将服务注册到注册中心
     *
     * @param rpcServiceName the rpcServiceName
     * @param address        the address
     * @throws Exception the exception
     */
    void register(@Nonnull String rpcServiceName, @Nonnull InetSocketAddress address) throws Exception;

    /**
     * server调用，将服务取消注册
     *
     * @param rpcServiceName the rpcServiceName
     * @param address        the address
     */
    void unregister(@Nonnull String rpcServiceName, @Nonnull InetSocketAddress address);

    /**
     * 查询服务地址，需要保证线程安全
     *
     * @param rpcServiceName the rpcServiceName
     * @return the address list
     */
    List<InetSocketAddress> lookup(@Nonnull String rpcServiceName);

    /**
     * 释放连接注册中心的资源
     */
    void close();
}
