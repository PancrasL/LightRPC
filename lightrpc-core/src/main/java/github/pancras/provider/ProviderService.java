package github.pancras.provider;

import github.pancras.wrapper.RpcServiceConfig;

/**
 * @author PancrasL 会存在并发访问，需要保证线程安全
 */
public interface ProviderService {
    /**
     * 将服务发布到注册中心，并添加到本地缓存
     *
     * @param rpcServiceConfig RPC 服务的相关属性
     */
    void publishService(RpcServiceConfig rpcServiceConfig) throws Exception;

    /**
     * 仅将服务添加到本地缓存
     *
     * @param rpcServiceConfig RPC 服务的相关属性
     */
    void addService(RpcServiceConfig rpcServiceConfig);

    /**
     * 服务调用，通过rpcServiceName获取服务实例
     *
     * @param rpcServiceName RPC服务名称
     * @return RPC服务实例
     */
    Object getService(String rpcServiceName);
}