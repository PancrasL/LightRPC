package github.pancras.config;

import github.pancras.config.constant.Constant;

/**
 * @author PancrasL
 */
public class SparrowServerConfig {
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
     * 服务器的默认监听地址（如果一个主机有多个ip，可以填写0.0.0.0）
     */
    public static String DEFAULT_SERVER_ADDRESS = Constant.DEFAULT_HOST_ADDRESS;

    /**
     * 服务器接的默认监听端口
     */
    public static int DEFAULT_SERVER_PORT = Constant.DEFAULT_HOST_PORT;

    /**
     * RPC服务的注册地址
     */
    public static String SERVICE_REGISTER_ADDRESS = Constant.DEFAULT_HOST_ADDRESS;

    private SparrowServerConfig() {
    }
}
