package github.pancras.config;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author pancras
 * @create 2021/6/9 13:52
 */
public class SparrowConfig {
    // 客户端连接服务器的超时时间
    public static int CONNECT_TIMEOUT_MILLIS = 5000;

    public static int PORT = 7998;

    public static String ZK_ADDRESS = "127.0.0.1:2181";

    // RPC服务器监听地址（如果一个主机有多个ip，可以填写0.0.0.0）
    public static String SERVER_ADDRESS;

    // RPC服务注册地址
    public static String SERVICE_ADDRESS;

    private SparrowConfig() {
    }

    static {
        try {
            SERVER_ADDRESS = InetAddress.getLocalHost().getHostAddress();
            SERVICE_ADDRESS = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            SERVER_ADDRESS = "127.0.0.1";
            SERVICE_ADDRESS = "127.0.0.1";
        }
    }
}
