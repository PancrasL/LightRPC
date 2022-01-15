package github.pancras.config;

import java.io.IOException;
import java.util.Properties;

/**
 * @author PancrasL
 */
public class DefaultConfig {
    /**
     * 如果lightrpc.properties有对应值，则使用该值，否则使用默认值
     */
    private static final Properties PROPERTIES = new Properties();
    /**
     * 默认的序列化器
     */
    public static String SERIALIZER_TYPE = getProperty("SERIALIZER_TYPE", "Kryo");
    /**
     * 服务器地址
     */
    public static String SERVER_ADDRESS = getProperty("SERVER_ADDRESS", "localhost:7998");
    /**
     * 注册中心地址
     */
    public static String REGISTRY = getProperty("REGISTRY", "zookeeper://localhost:2181");

    private DefaultConfig() {
    }

    private static String getProperty(String key, String defaultValue) {
        try {
            PROPERTIES.load(DefaultConfig.class.getResourceAsStream("/lightrpc.properties"));
        } catch (IOException e) {
            return defaultValue;
        }
        return PROPERTIES.getProperty(key, defaultValue);
    }
}
