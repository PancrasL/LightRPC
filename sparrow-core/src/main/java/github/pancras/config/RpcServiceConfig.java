package github.pancras.config;

/**
 * @author pancras
 * @create 2021/6/3 20:03
 */
public class RpcServiceConfig {

    private Object service;

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
