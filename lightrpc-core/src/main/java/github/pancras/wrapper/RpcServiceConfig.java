package github.pancras.wrapper;

/**
 * @author PancrasL
 * <p>
 * RPC服务的包装类
 */
public class RpcServiceConfig {

    /**
     * 当接口有多个实现类时，使用group来标识
     */
    private String group = "";
    /**
     * 标识接口的不同版本，不同版本不兼容
     */
    private String version = "";
    private Object service;

    public RpcServiceConfig(Object service, String group, String version) {
        this.group = group;
        this.version = version;
        this.service = service;
    }

    public RpcServiceConfig(Object service) {
        this.service = service;
    }

    public String getRpcServiceName() {
        return group + '@' + version + '@' + getServiceName();
    }

    private String getServiceName() {
        return this.service.getClass().getInterfaces()[0].getCanonicalName();
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Object getService() {
        return service;
    }

    public void setService(Object service) {
        this.service = service;
    }
}
