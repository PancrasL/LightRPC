package github.pancras.provider;

import github.pancras.config.RpcServiceConfig;

/**
 * @author pancras
 * @create 2021/6/3 20:06
 */
public interface ServiceProvider {
    /**
     * @param rpcServiceConfig RPC 服务的相关属性
     */
    void publishService(RpcServiceConfig rpcServiceConfig);

    /**
     * @param rpcServiceConfig RPC 服务的相关属性
     */
    void addService(RpcServiceConfig rpcServiceConfig);

    /**
     * @param rpcServiceName RPC服务名称
     * @return RPC服务对象
     */
    Object getService(String rpcServiceName);
}
