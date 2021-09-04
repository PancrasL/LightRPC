package github.pancras.registry;

import java.net.InetSocketAddress;

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
    void register(String rpcServiceName, InetSocketAddress address) throws Exception;

    /**
     * 服务提供者使用，将服务取消注册
     *
     * @param rpcServiceName the rpcServiceName
     * @param address        the address
     * @throws Exception the exception
     */
    void unregister(String rpcServiceName, InetSocketAddress address) throws Exception;

    /**
     * 服务消费者使用，查询服务地址
     *
     * @param rpcServiceName the rpcServiceName
     * @return the address list
     * @throws Exception the exception
     */
    InetSocketAddress lookup(String rpcServiceName) throws Exception;

    /**
     * 释放连接注册中心的资源
     *
     * @throws Exception the exception
     */
    void close() throws Exception;
}
