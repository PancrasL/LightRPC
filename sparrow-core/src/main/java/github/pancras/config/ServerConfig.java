package github.pancras.config;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author pancras
 * @create 2021/6/9 13:52
 */
public class ServerConfig {
    public static int PORT = 7998;

    public static String ZK_ADDRESS = "127.0.0.1:2181";

    // RPC server listen address
    public static String SERVER_ADDRESS;

    // Service registered address
    public static String SERVICE_ADDRESS;

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
