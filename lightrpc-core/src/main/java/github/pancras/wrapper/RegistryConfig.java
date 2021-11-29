package github.pancras.wrapper;

/**
 * @author PancrasL
 */
public class RegistryConfig {
    private final String type;
    private final String host;
    private final int port;

    private RegistryConfig(String type, String host, int port) {
        this.type = type;
        this.host = host;
        this.port = port;
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
