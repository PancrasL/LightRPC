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

    private RedisRegistryServiceImpl(InetSocketAddress socketAddress) {
        this.jedis = buildJedis(socketAddress);
    }

    public static RedisRegistryServiceImpl newInstance(InetSocketAddress socketAddress) {
        return new RedisRegistryServiceImpl(socketAddress);
    }

    private static Jedis buildJedis(InetSocketAddress socketAddress) {
        Jedis jedis;
        // 连接redis，最多等待3s
        jedis = new Jedis(socketAddress.getHostName(), socketAddress.getPort(), 3000);
        jedis.connect();
        return jedis;
    }

    @Override
    public void register(@Nonnull String rpcServiceName, @Nonnull InetSocketAddress address, @Nonnull Integer weight) {
        String key = JedisUtils.REDIS_REGISTER_ROOT_PATH + "/" + rpcServiceName;
        String value = address.toString();
        JedisUtils.createNode(jedis, key, value);
    }

    @Override
    public List<String> lookup(@Nonnull String rpcServiceName) {
        String key = JedisUtils.REDIS_REGISTER_ROOT_PATH + "/" + rpcServiceName;
        return JedisUtils.getNodes(jedis, key);
    }

    @Override
    public void close() {

    }
}
