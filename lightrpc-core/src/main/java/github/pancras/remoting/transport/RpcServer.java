package github.pancras.remoting.transport;

import javax.annotation.Nonnull;
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
    void registerService(@Nonnull RpcServiceConfig<?> rpcServiceConfig) throws Exception;
}
