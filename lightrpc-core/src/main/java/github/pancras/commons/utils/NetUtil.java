package github.pancras.commons.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * @author PancrasL
 */
public class NetUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(NetUtil.class);

    public static String toStringAddress(InetSocketAddress address) {
        return address.getAddress().getHostAddress() + ":" + address.getPort();
    }

    public static InetSocketAddress toInetSocketAddress(String address) {
        String[] ipAndPort = address.split(":");
        return new InetSocketAddress(ipAndPort[0], Integer.parseInt(ipAndPort[1]));
    }
}
