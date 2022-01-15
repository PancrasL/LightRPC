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
    private final String group;
    /**
     * 标识接口的不同版本，不同版本不兼容
     */
    private final String version;
    /**
     * 标识服务的权重，默认为1
     */
    private final Integer weight;
    /**
     * 服务名称
     */
    private final String serviceName;
    /**
     * 服务实例
     */
    private final T service;

    private RpcServiceConfig(Builder<T> builder) {
        this.group = builder.group;
        this.version = builder.version;
        this.service = builder.service;
        this.weight = builder.weight;
        this.serviceName = group + '@' + version + '@' + builder.serviceName;
    }

    public static <T> RpcServiceConfig<T> newDefaultConfig(T service) {
        return new RpcServiceConfig.Builder<T>(service).build();
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getGroup() {
        return group;
    }

    public String getVersion() {
        return version;
    }

    public Object getService() {
        return service;
    }

    public Integer getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return "RpcServiceConfig{" +
                "group='" + group + '\'' +
                ", version='" + version + '\'' +
                ", weight=" + weight +
                ", serviceName='" + serviceName + '\'' +
                ", service=" + service +
                '}';
    }

    public static class Builder<T> {
        // Required paramaters
        private final T service;

        // Optional parameters - initialized to default values
        private String group = "";
        private String version = "";
        private Integer weight = 1;
        // 默认是服务实例的全限定名
        private String serviceName;

        public Builder(T service) {
            this.service = service;
            this.serviceName = service.getClass().getInterfaces()[0].getCanonicalName();
        }

        public Builder<T> group(String group) {
            this.group = group;
            return this;
        }

        public Builder<T> version(String version) {
            this.version = version;
            return this;
        }

        public Builder<T> weight(Integer weight) {
            this.weight = weight;
            return this;
        }

        public Builder<T> serviceName(String serviceName) {
            this.serviceName = serviceName;
            return this;
        }

        public RpcServiceConfig<T> build() {
            return new RpcServiceConfig<>(this);
        }
    }
}
