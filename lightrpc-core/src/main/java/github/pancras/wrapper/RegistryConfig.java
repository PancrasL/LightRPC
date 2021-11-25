package github.pancras.wrapper;

import github.pancras.config.DefaultConfig;

public class RegistryConfig {
    private final String type;
    private final String host;
    private final int port;

    private RegistryConfig(String type, String host, int port) {
        this.type = type;
        this.host = host;
        this.port = port;
    }

    public static RegistryConfig getDefaultConfig() {
        return DefaultConfig.DEFAULT_REGISTRY_CONFIG;
    }

    public static RegistryConfig newConfig(String type, String host, int port) {
        return new RegistryConfig(type, host, port);
    }

    public String getType() {
        return type;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }
}
