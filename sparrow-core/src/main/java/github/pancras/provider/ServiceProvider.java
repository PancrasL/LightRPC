package github.pancras.provider;

import github.pancras.config.RpcServiceConfig;

/**
 * @author pancras
 * @create 2021/6/3 20:06
 */
public interface ServiceProvider {
    void addService(RpcServiceConfig rpcServiceConfig);

    Object getService(String rpcServiceName);
}
