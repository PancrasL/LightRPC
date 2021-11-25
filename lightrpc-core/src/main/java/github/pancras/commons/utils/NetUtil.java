package github.pancras.commons.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import javax.annotation.Nullable;

/**
 * @author PancrasL
 */
public class NetUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(NetUtil.class);
    private static final String LOCAL_HOST_IP = "127.0.0.1";
    private static final String ANY_HOST = "0.0.0.0";

    public static String getLocalHost() {
        InetAddress address = getLocalAddress();
        return address == null ? "localhost" : address.getHostName();
    }

    public static String getLocalIp() {
        InetAddress address = getLocalAddress();
        return address == null ? LOCAL_HOST_IP : address.getHostAddress();
    }

    @Nullable
    public static InetAddress getLocalAddress() {
        InetAddress address = null;
        try {
            address = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            LOGGER.warn("Failed to get host ip address, {}", e.getMessage());
        }
        return address;
    }

    public static void validAddress(InetSocketAddress address) {
        if (address.getHostName() == null || 0 == address.getPort()) {
            throw new IllegalArgumentException("Invalid address:" + address);
        }
    }

    public static String toStringAddress(InetSocketAddress address) {
        return address.getAddress().getHostAddress() + ":" + address.getPort();
    }
}
