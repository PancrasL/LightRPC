package github.pancras.provider;

import java.io.Closeable;

import github.pancras.config.wrapper.RpcServiceConfig;

/**
 * @author pancras
 * @create 2021/6/3 20:06
 */
public interface ServiceProvider extends Closeable {
    /**
     * 将服务发布到注册中心，并添加到本地缓存
     *
     * @param rpcServiceConfig RPC 服务的相关属性
     */
    void publishService(RpcServiceConfig rpcServiceConfig);

    /**
     * 仅将服务添加到本地缓存
     *
     * @param rpcServiceConfig RPC 服务的相关属性
     */
    void addService(RpcServiceConfig rpcServiceConfig);

    /**
     * 从本地缓存获取服务地址，如果缓存中没有，则从注册中心中查询
     *
     * @param rpcServiceName RPC服务名称
     * @return RPC服务对象
     */
    Object getService(String rpcServiceName);
}
