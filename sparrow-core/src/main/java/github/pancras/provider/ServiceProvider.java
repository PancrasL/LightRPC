package github.pancras.provider;

import github.pancras.config.RpcServiceConfig;

/**
 * @author pancras
 * @create 2021/6/3 20:06
 */
public interface ServiceProvider {
    /**
     * @param rpcServiceConfig RPC service related attributes
     */
    void publishService(RpcServiceConfig rpcServiceConfig);

    /**
     * @param rpcServiceConfig RPC service related attributes
     */
    void addService(RpcServiceConfig rpcServiceConfig);

    /**
     * @param rpcServiceName RPC service name
     * @return service Object
     */
    Object getService(String rpcServiceName);
}
