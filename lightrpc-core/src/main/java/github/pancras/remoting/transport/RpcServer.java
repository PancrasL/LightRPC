package github.pancras.remoting.transport;

import javax.annotation.concurrent.ThreadSafe;

import github.pancras.commons.Disposable;
import github.pancras.wrapper.RpcServiceConfig;

/**
 * @author PancrasL
 */
@ThreadSafe
public interface RpcServer extends Disposable {
    /**
     * 向注册中心注册服务
     *
     * @param rpcServiceConfig 服务的包装类
     */
    void registerService(RpcServiceConfig<?> rpcServiceConfig) throws Exception;

    /**
     * 使用默认ip和默认port启动服务器，详见SparrowConfig
     *
     * @throws Exception 服务器启动异常
     */
    void start() throws Exception;
}
