package github.pancras.wrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import github.pancras.proxy.RpcClientProxy;
import github.pancras.remoting.transport.RpcClient;

public class RpcReferenceConfig<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RpcReferenceConfig.class);

    private final RpcClient rpcClient;
    private final String group;
    private final String version;
    private final Class<T> interfac;
    private Object referent;

    public static class Builder<T> {
        // Required paramaters
        private final Class<T> interfac;
        private final RpcClient rpcClient;

        // Optional parameters - initialized to default values
        private String group = "";
        private String version = "";

        public Builder(RpcClient rpcClient, Class<T> interfac) {
            this.rpcClient = rpcClient;
            this.interfac = interfac;
        }

        public Builder<T> group(String group) {
            this.group = group;
            return this;
        }

        public Builder<T> version(String version) {
            this.version = version;
            return this;
        }

        public RpcReferenceConfig<T> build() {
            return new RpcReferenceConfig<>(this);
        }
    }

    private RpcReferenceConfig(Builder<T> builder) {
        this.group = builder.group;
        this.version = builder.version;
        this.interfac = builder.interfac;
        this.rpcClient = builder.rpcClient;
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

    public T getReferent() {
        if (referent == null) {
            referent = RpcClientProxy.newProxyInstance(this);
        }
        return (T) referent;
    }
}
