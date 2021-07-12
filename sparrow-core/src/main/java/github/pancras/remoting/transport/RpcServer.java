package github.pancras.remoting.transport;

import java.io.Closeable;

import github.pancras.config.RpcServiceConfig;

/**
 * @author pancras
 * @create 2021/6/17 10:00
 */
public interface RpcServer extends Closeable {
    void registerService(RpcServiceConfig rpcServiceConfig);

    void start() throws Exception;

    @Override
    void close();
}
