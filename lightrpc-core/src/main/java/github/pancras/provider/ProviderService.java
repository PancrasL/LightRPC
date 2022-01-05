package github.pancras.provider;

import java.net.InetSocketAddress;

import javax.annotation.concurrent.ThreadSafe;

import github.pancras.wrapper.RpcServiceConfig;

/**
 * 保存和提供服务实例
 *
 * @author PancrasL
 */
@ThreadSafe
public interface ProviderService {
    /**
     * 将服务发布到注册中心
     *
     * @param rpcServiceConfig RPC 服务的相关属性
     * @param address
     */
    void publishService(RpcServiceConfig<?> rpcServiceConfig, InetSocketAddress address) throws Exception;

    /**
     * 通过rpcServiceName获取服务实例
     *
     * @param rpcServiceName RPC服务名称
     * @return RPC服务实例
     */
    Object getServiceInstance(String rpcServiceName);

    /**
     * 释放注册中心的资源
     */
    void close();
}
