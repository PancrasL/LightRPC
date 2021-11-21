package github.pancras.wrapper;

/**
 * @author PancrasL
 * <p>
 * RPC服务的包装类
 */
public class RpcServiceConfig<T> {

    /**
     * 当接口有多个实现类时，使用group来标识
     */
    private String group;
    /**
     * 标识接口的不同版本，不同版本不兼容
     */
    private String version;
    /**
     * 客户端表示接口、服务端表示接口的实例
     */
    private T service;

    private RpcServiceConfig(T service, String group, String version) {
        this.group = group;
        this.version = version;
        this.service = service;
    }

    /**
     * 使用默认的group和version
     */
    public static <T> RpcServiceConfig<T> newInstance(T service) {
        // 使用“”代表默认group和默认version
        return new RpcServiceConfig<>(service, "", "");
    }

    /**
     * 使用指定的group和version
     */
    public static <T> RpcServiceConfig<T> newInstance(T service, String group, String version) {
        return new RpcServiceConfig<>(service, group, version);
    }

    public RpcServiceConfig(T service) {
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

    public void setService(T service) {
        this.service = service;
    }
}
