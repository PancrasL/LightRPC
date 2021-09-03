package github.pancras.remoting.transport;

import github.pancras.commons.Disposable;
import github.pancras.config.wrapper.RpcServiceConfig;

/**
 * @author PancrasL
 */
public interface RpcServer extends Disposable {
    /**
     * 向注册中心注册服务
     *
     * @param rpcServiceConfig 服务的包装类
     */
    void registerService(RpcServiceConfig rpcServiceConfig);

    /**
     * 使用默认ip和默认port启动服务器，详见SparrowConfig
     *
     * @throws Exception 服务器启动异常
     */
    void start() throws Exception;

    /**
     * 使用指定ip和指定port启动服务器
     *
     * @param host 指定ip
     * @param port 指定port
     * @throws Exception 服务器启动异常
     */
    void start(String host, int port) throws Exception;
}
