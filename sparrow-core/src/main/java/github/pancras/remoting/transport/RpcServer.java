package github.pancras.remoting.transport;

import github.pancras.config.RpcServiceConfig;

/**
 * @author pancras
 * @create 2021/6/17 10:00
 */
public interface RpcServer {
    void registerService(RpcServiceConfig rpcServiceConfig);

    void start() throws Exception;
}
