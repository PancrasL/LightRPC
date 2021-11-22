package github.pancras.wrapper;

import github.pancras.remoting.transport.RpcServer;

/**
 * @author PancrasL
 * <p>
 * RPC服务的包装类
 */
public class RpcServiceConfig<T> {

    /**
     * 服务通信
     */
    private final RpcServer rpcServer;
    /**
     * 当接口有多个实现类时，使用group来标识
     */
    private final String group;
    /**
     * 标识接口的不同版本，不同版本不兼容
     */
    private final String version;
    /**
     * 服务实例
     */
    private final T service;

    /**
     * 暴露及注册服务
     */
    public void export() throws Exception {
        rpcServer.registerService(this);
    }

    public static class Builder<T> {
        // Required paramaters
        private final RpcServer rpcServer;
        private final T service;

        // Optional parameters - initialized to default values
        private String group = "";
        private String version = "";

        public Builder(RpcServer rpcServer, T service) {
            this.rpcServer = rpcServer;
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

        public RpcServiceConfig<T> build() {
            return new RpcServiceConfig<>(this);
        }
    }

    private RpcServiceConfig(Builder<T> builder) {
        this.rpcServer = builder.rpcServer;
        this.group = builder.group;
        this.version = builder.version;
        this.service = builder.service;
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
}
