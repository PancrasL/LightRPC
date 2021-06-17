package github.pancras.remoting.transport.netty.client;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.netty.channel.Channel;

/**
 * @author pancras
 * @create 2021/6/24 10:11 重用 Channel 避免重复连接服务端
 */
public class ChannelPool {
    private final Map<String, Channel> poolMap;

    public ChannelPool() {
        poolMap = new ConcurrentHashMap<>();
    }

    public Channel getOrNull(InetSocketAddress inetSocketAddress) {
        String key = inetSocketAddress.toString();
        if (poolMap.containsKey(key)) {
            Channel channel = poolMap.get(key);
            if (channel != null && channel.isActive()) {
                return channel;
            } else {
                poolMap.remove(key);
            }
        }
        return null;
    }

    public void set(InetSocketAddress inetSocketAddress, Channel channel) {
        String key = inetSocketAddress.toString();
        poolMap.put(key, channel);
    }

    public void remove(InetSocketAddress inetSocketAddress) {
        String key = inetSocketAddress.toString();
        poolMap.remove(key);
    }
}