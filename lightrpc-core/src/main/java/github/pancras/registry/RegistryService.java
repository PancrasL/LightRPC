package github.pancras.registry;

import java.net.InetSocketAddress;

import javax.annotation.Nonnull;

/**
 * @author PancrasL
 */
public interface RegistryService {
    /**
     * 服务提供者使用，将服务注册到注册中心
     *
     * @param rpcServiceName the rpcServiceName
     * @param address        the address
     * @throws Exception the exception
     */
    void register(@Nonnull String rpcServiceName, @Nonnull InetSocketAddress address) throws Exception;

    /**
     * 服务提供者使用，将服务取消注册
     *
     * @param rpcServiceName the rpcServiceName
     * @param address        the address
     */
    void unregister(@Nonnull String rpcServiceName, @Nonnull InetSocketAddress address);

    /**
     * 服务消费者使用，查询服务地址，需要保证线程安全
     *
     * @param rpcServiceName the rpcServiceName
     * @return the address list
     */
    InetSocketAddress lookup(@Nonnull String rpcServiceName) throws Exception;

    /**
     * 释放连接注册中心的资源
     */
    void close();
}
