package github.pancras.wrapper;

/**
 * 包装服务，包含了服务的group、version信息
 */
public class ServiceWrapper {
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
    private Object service;

    private ServiceWrapper(Object service, String group, String version) {
        this.group = group;
        this.version = version;
        this.service = service;
    }

    /**
     * 使用默认的group和version，客户端传入接口的类信息
     *
     * @param service the service
     * @return service的包装对象
     */
    public static ServiceWrapper newInstance(Object service) {
        // 使用“”代表默认group和默认version
        return new ServiceWrapper(service, "", "");
    }

    /**
     * 使用指定的group和version
     *
     * @param service the service，客户端传入接口的类信息
     * @param group   the group
     * @param version the version
     * @return service的包装对象
     */
    public static ServiceWrapper newInstance(Object service, String group, String version) {
        return new ServiceWrapper(service, group, version);
    }

    public ServiceWrapper(Object service) {
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
