package github.pancras.config;

/**
 * @author pancras
 * @create 2021/6/9 13:52
 */
public class SparrowConfig {
    /**
     * 客户端连接服务器的超时时间
     */
    public static int CONNECT_TIMEOUT_MILLIS = 5000;
    /**
     * 服务器接收连接的端口
     */
    public static int PORT = 7998;

    /**
     * 默认的注册中心
     */
    public static String DEFAULT_REGISRY_TYPE = "redis";
    /**
     * Zookeeper服务器的ip
     */
    public static String DEFAULT_ZK_ADDRESS = "127.0.0.1";
    /**
     * Zookeeper服务器的port
     */
    public static int DEFAULT_ZK_PORT = 2181;
    /**
     * Redis服务器的ip
     */
    public static String DEFAULT_REDIS_ADDRESS = "127.0.0.1";
    /**
     * Redis服务器的port
     */
    public static int DEFAULT_REDIS_PORT = 6379;

    /**
     * RPC服务器监听地址（如果一个主机有多个ip，可以填写0.0.0.0）
     */
    public static String SERVER_LISTEN_ADDRESS = "127.0.0.1";

    /**
     * RPC服务的注册地址
     */
    public static String SERVICE_REGISTER_ADDRESS = "127.0.0.1";

    private SparrowConfig() {
    }
}
