package github.pancras.registry.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.List;

import javax.annotation.Nonnull;

import github.pancras.registry.RegistryService;
import github.pancras.registry.redis.util.JedisUtils;
import redis.clients.jedis.Jedis;

/**
 * @author PancrasL
 */
public class RedisRegistryServiceImpl implements RegistryService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisRegistryServiceImpl.class);

    private final Jedis jedis;

    public static RedisRegistryServiceImpl newInstance(InetSocketAddress socketAddress) {
        return new RedisRegistryServiceImpl(socketAddress);
    }

    private RedisRegistryServiceImpl(InetSocketAddress socketAddress) {
        this.jedis = buildJedis(socketAddress);
    }

    @Override
    public void register(@Nonnull String rpcServiceName, @Nonnull InetSocketAddress address) {
        String key = JedisUtils.REDIS_REGISTER_ROOT_PATH + "/" + rpcServiceName;
        String value = address.toString();
        JedisUtils.createNode(jedis, key, value);
    }

    @Override
    public void unregister(@Nonnull String rpcServiceName, @Nonnull InetSocketAddress address) {

    }

    @Override
    public InetSocketAddress lookup(@Nonnull String rpcServiceName) {
        String key = JedisUtils.REDIS_REGISTER_ROOT_PATH + "/" + rpcServiceName;
        List<String> serviceUrls = JedisUtils.getNodes(jedis, key);
        if (serviceUrls.isEmpty()) {
            throw new RuntimeException(String.format("Service %s not found", rpcServiceName));
        }
        double randomNum = Math.random() * serviceUrls.size();
        String targetServiceUrl = serviceUrls.get((int) randomNum);
        String[] hostPort = targetServiceUrl.split(":");
        LOGGER.debug("Get service: [{}]", targetServiceUrl);
        return new InetSocketAddress(hostPort[0], Integer.parseInt(hostPort[1]));
    }

    @Override
    public void close() {

    }

    private static Jedis buildJedis(InetSocketAddress socketAddress) {
        Jedis jedis;
        // 连接redis，最多等待3s
        jedis = new Jedis(socketAddress.getHostName(), socketAddress.getPort(), 3000);
        jedis.connect();
        return jedis;
    }
}
