package github.pancras.wrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import github.pancras.proxy.RpcReferenceProxy;
import github.pancras.remoting.transport.RpcClient;

/**
 * @author PancrasL
 */
public class RpcReferenceConfig<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RpcReferenceConfig.class);

    private final RpcClient rpcClient;
    private final String group;
    private final String version;
    private final Class<T> interfac;
    // 服务名，默认是 group + '@' + version + '@' + interfaceName;
    private final String serviceName;
    // Rpc服务实例的代理对象，将调用请求通过网络发送给远端的实例执行
    private Object referent;

    private RpcReferenceConfig(Builder<T> builder) {
        this.group = builder.group;
        this.version = builder.version;
        this.interfac = builder.interfac;
        this.rpcClient = builder.rpcClient;
        this.serviceName = group + '@' + version + '@' + builder.serviceName;
    }

    public static <T> RpcReferenceConfig<T> newDefaultConfig(RpcClient rpcClient, Class<T> interfac) {
        return new RpcReferenceConfig.Builder<T>(rpcClient, interfac).build();
    }

    public RpcClient getRpcClient() {
        return rpcClient;
    }

    public String getGroup() {
        return group;
    }

    public String getVersion() {
        return version;
    }

    public Class<T> getInterface() {
        return interfac;
    }

    public String getServiceName() {
        return serviceName;
    }

    public T getReferent() {
        if (referent == null) {
            referent = RpcReferenceProxy.newProxyInstance(this);
        }
        return (T) referent;
    }

    @Override
    public String toString() {
        return "RpcReferenceConfig{" +
                "rpcClient=" + rpcClient +
                ", group='" + group + '\'' +
                ", version='" + version + '\'' +
                ", interfac=" + interfac +
                ", serviceName='" + serviceName + '\'' +
                ", referent=" + referent +
                '}';
    }

    public static class Builder<T> {
        // Required paramaters
        private final Class<T> interfac;
        private final RpcClient rpcClient;

        // Optional parameters - initialized to default values
        private String group = "";
        private String version = "";
        private String serviceName;

        public Builder(RpcClient rpcClient, Class<T> interfac) {
            this.rpcClient = rpcClient;
            this.interfac = interfac;
            this.serviceName = interfac.getCanonicalName();
        }

        public Builder<T> group(String group) {
            this.group = group;
            return this;
        }

        public Builder<T> version(String version) {
            this.version = version;
            return this;
        }

        public Builder<T> serviceName(String serviceName) {
            this.serviceName = serviceName;
            return this;
        }

        public RpcReferenceConfig<T> build() {
            return new RpcReferenceConfig<>(this);
        }
    }
}
