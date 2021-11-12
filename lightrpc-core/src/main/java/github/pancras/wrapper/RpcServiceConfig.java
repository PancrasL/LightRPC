package github.pancras.wrapper;

/**
 * @author PancrasL
 * <p>
 * RPC服务的包装类
 */
public class RpcServiceConfig {

    private Object service;

    public RpcServiceConfig() {
    }

    public RpcServiceConfig(Object service) {
        this.service = service;
    }

    public String getRpcServiceName() {
        return this.service.getClass().getInterfaces()[0].getCanonicalName();
    }

    public Object getService() {
        return service;
    }

    public void setService(Object service) {
        this.service = service;
    }
}
