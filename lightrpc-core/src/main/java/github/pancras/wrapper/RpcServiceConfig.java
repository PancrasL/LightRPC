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
     * 服务实例
     */
    private final T service;

    private RpcServiceConfig(Builder<T> builder) {
        this.group = builder.group;
        this.version = builder.version;
        this.service = builder.service;
        this.weight = builder.weight;
    }

    public static <T> RpcServiceConfig<T> newDefaultConfig(T service) {
        return new RpcServiceConfig.Builder<T>(service).build();
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

    public String getVersion() {
        return version;
    }

    public Object getService() {
        return service;
    }

    public Integer getWeight() {
        return weight;
    }

    public static class Builder<T> {
        // Required paramaters
        private final T service;

        // Optional parameters - initialized to default values
        private String group = "";
        private String version = "";
        private Integer weight = 1;

        public Builder(T service) {
            this.service = service;
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

        public RpcServiceConfig<T> build() {
            return new RpcServiceConfig<>(this);
        }
    }
}
