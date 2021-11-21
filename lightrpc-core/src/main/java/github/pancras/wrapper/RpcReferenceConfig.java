package github.pancras.wrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import github.pancras.proxy.RpcClientProxy;
import github.pancras.remoting.transport.RpcClient;

public class RpcReferenceConfig<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RpcReferenceConfig.class);

    private final RpcClientProxy rpcClientProxy;
    private final ServiceWrapper serviceWrapper;
    private Class<T> interfac;
    private Object referent;

    private RpcReferenceConfig(RpcClient rpcClient, ServiceWrapper serviceWrapper) {
        this.rpcClientProxy = RpcClientProxy.newInstance(rpcClient, serviceWrapper);
        this.serviceWrapper = serviceWrapper;
        this.interfac = (Class<T>) serviceWrapper.getService();
    }

    /**
     * 创建client和rpcServiceConfig的代理类，可以通过这个类的getReferent()方法获取指定接口的代理对象
     *
     * @param client  the client，执行通信过程
     * @param service 提供service的接口、group和version信息
     * @return the config
     */
    public static <T> RpcReferenceConfig<T> newInstance(RpcClient client, ServiceWrapper service) {
        return new RpcReferenceConfig<>(client, service);
    }

    public T getReferent() {
        if (referent == null) {
            referent = rpcClientProxy.getProxy(interfac);
        }
        return (T) referent;
    }
}
