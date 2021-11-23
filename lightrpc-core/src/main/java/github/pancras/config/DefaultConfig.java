package github.pancras.config;

import github.pancras.commons.utils.PropertyUtil;

/**
 * @author PancrasL
 */
public class DefaultConfig {
    /**
     * 默认的注册中心
     */
    public static String DEFAULT_REGISRY_TYPE;
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
     * 服务器的默认监听地址（如果一个主机有多个ip，可以填写0.0.0.0）
     */
    public static String DEFAULT_SERVER_ADDRESS;

    /**
     * 服务器接的默认监听端口
     */
    public static int DEFAULT_SERVER_PORT;

    /**
     * RPC服务的注册地址
     */
    public static String SERVICE_REGISTER_ADDRESS;

    static {
        DEFAULT_REGISRY_TYPE = PropertyUtil.getProperty("DEFAULT_REGISRY_TYPE", ConstantValue.ZOOKEEPER);
        DEFAULT_ZK_ADDRESS = PropertyUtil.getProperty("DEFAULT_ZK_ADDRESS", ConstantValue.DEFAULT_HOST_ADDRESS);
        DEFAULT_ZK_PORT = Integer.parseInt(PropertyUtil.getProperty("DEFAULT_ZK_PORT", "2181"));
        DEFAULT_REDIS_ADDRESS = PropertyUtil.getProperty("DEFAULT_REDIS_ADDRESS", ConstantValue.DEFAULT_HOST_ADDRESS);
        DEFAULT_REDIS_PORT = Integer.parseInt(PropertyUtil.getProperty("DEFAULT_REDIS_PORT", "6379"));
        DEFAULT_SERIALIZER_TYPE = PropertyUtil.getProperty("DEFAULT_SERIALIZER_TYPE", ConstantValue.KRYO);
        DEFAULT_SERVER_ADDRESS = PropertyUtil.getProperty("DEFAULT_SERVER_ADDRESS", ConstantValue.DEFAULT_HOST_ADDRESS);
        DEFAULT_SERVER_PORT = Integer.parseInt(PropertyUtil.getProperty("DEFAULT_SERVER_PORT", "7998"));
        SERVICE_REGISTER_ADDRESS = PropertyUtil.getProperty("SERVICE_REGISTER_ADDRESS", ConstantValue.DEFAULT_HOST_ADDRESS);
    }

    private DefaultConfig() {
    }
}
