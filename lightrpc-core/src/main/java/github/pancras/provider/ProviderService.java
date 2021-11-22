package github.pancras.provider;

import javax.annotation.concurrent.ThreadSafe;

import github.pancras.wrapper.RpcServiceConfig;

/**
 * @author PancrasL 会存在并发访问，需要保证线程安全
 */
@ThreadSafe
public interface ProviderService {
    /**
     * 将服务发布到注册中心，并添加到本地缓存
     *
     * @param rpcServiceConfig RPC 服务的相关属性
     */
    void publishService(RpcServiceConfig<?> rpcServiceConfig) throws Exception;

    /**
     * 服务调用，通过rpcServiceName获取服务实例
     *
     * @param rpcServiceName RPC服务名称
     * @return RPC服务实例
     */
    Object getService(String rpcServiceName);

    /**
     * 释放注册中心的资源
     */
    void close();
}
