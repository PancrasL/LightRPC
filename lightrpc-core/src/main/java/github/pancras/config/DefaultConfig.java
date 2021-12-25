package github.pancras.config;

import github.pancras.commons.utils.PropertyUtil;
import github.pancras.wrapper.RegistryConfig;

/**
 * @author PancrasL
 */
public class DefaultConfig {
    public static RegistryConfig DEFAULT_REGISTRY_CONFIG;
    /**
     * 默认的注册中心
     */
    public static String DEFAULT_REGISTRY_TYPE;
    /**
     * Zookeeper服务器的ip
     */
    public static String DEFAULT_ZK_ADDRESS;
    /**
     * Zookeeper服务器的port
     */
    public static int DEFAULT_ZK_PORT;
    /**
     * Redis服务器的ip
     */
    public static String DEFAULT_REDIS_ADDRESS;
    /**
     * Redis服务器的port
     */
    public static int DEFAULT_REDIS_PORT;

    /**
     * 默认的序列化器
     */
    public static String DEFAULT_SERIALIZER_TYPE;

    /**
     * 服务器地址
     */
    public static String DEFAULT_SERVER_ADDRESS;

    /**
     * 服务器的默认监听端口
     */
    public static int DEFAULT_SERVER_PORT;

    static {
        DEFAULT_REGISTRY_TYPE = PropertyUtil.getProperty("DEFAULT_REGISRY_TYPE", "zookeeper");
        DEFAULT_ZK_ADDRESS = PropertyUtil.getProperty("DEFAULT_ZK_ADDRESS", "127.0.0.1");
        DEFAULT_ZK_PORT = Integer.parseInt(PropertyUtil.getProperty("DEFAULT_ZK_PORT", "2181"));
        DEFAULT_REDIS_ADDRESS = PropertyUtil.getProperty("DEFAULT_REDIS_ADDRESS", "127.0.0.1");
        DEFAULT_REDIS_PORT = Integer.parseInt(PropertyUtil.getProperty("DEFAULT_REDIS_PORT", "6379"));
        DEFAULT_SERIALIZER_TYPE = PropertyUtil.getProperty("DEFAULT_SERIALIZER_TYPE", "kryo");
        DEFAULT_SERVER_ADDRESS = PropertyUtil.getProperty("DEFAULT_SERVER_ADDRESS", "127.0.0.1");
        DEFAULT_SERVER_PORT = Integer.parseInt(PropertyUtil.getProperty("DEFAULT_SERVER_PORT", "7998"));
        if ("zookeeper".equals(DEFAULT_REGISTRY_TYPE)) {
            DEFAULT_REGISTRY_CONFIG = RegistryConfig.newConfig(DEFAULT_REGISTRY_TYPE, DEFAULT_ZK_ADDRESS, DEFAULT_ZK_PORT);
        } else if ("redis".equals(DEFAULT_REGISTRY_TYPE)) {
            DEFAULT_REGISTRY_CONFIG = RegistryConfig.newConfig(DEFAULT_REGISTRY_TYPE, DEFAULT_REDIS_ADDRESS, DEFAULT_REDIS_PORT);
        }
    }

    private DefaultConfig() {
    }
}
