package github.pancras.config;

import github.pancras.config.constant.Constant;

/**
 * @author PancrasL
 */
public class SparrowClientConfig {
    /**
     * 客户端连接服务器的超时时间
     */
    public static int CONNECT_TIMEOUT_MILLIS = 5000;

    /**
     * 默认的注册中心
     */
    public static String DEFAULT_REGISRY_TYPE = Constant.ZOOKEEPER;

    /**
     * Zookeeper服务器的ip
     */
    public static String DEFAULT_ZK_ADDRESS = Constant.DEFAULT_HOST_ADDRESS;

    /**
     * Zookeeper服务器的port
     */
    public static int DEFAULT_ZK_PORT = 2181;

    /**
     * Redis服务器的ip
     */
    public static String DEFAULT_REDIS_ADDRESS = Constant.DEFAULT_HOST_ADDRESS;
    /**
     * Redis服务器的port
     */
    public static int DEFAULT_REDIS_PORT = 6379;

    /**
     * 默认的序列化器
     */
    public static String DEFAULT_SERIALIZER_TYPE = Constant.KRYO;

    /**
     * RPC服务的注册地址
     */
    public static String SERVICE_REGISTER_ADDRESS = Constant.DEFAULT_HOST_ADDRESS;

    private SparrowClientConfig() {
    }
}
