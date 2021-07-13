package github.pancras.registry.redis;

import java.net.InetSocketAddress;

import github.pancras.registry.ServiceRegistry;
import github.pancras.registry.redis.util.JedisUtils;
import redis.clients.jedis.Jedis;

/**
 * @author PancrasL
 */
public class RedisServiceRegistryImpl implements ServiceRegistry {

    private final Jedis jedis;

    public RedisServiceRegistryImpl() {
        jedis = JedisUtils.getRedisClient();
    }

    @Override
    public void registerService(String rpcServiceName, InetSocketAddress inetSocketAddress) {
        String key = JedisUtils.REDIS_REGISTER_ROOT_PATH + "/" + rpcServiceName;
        String value = inetSocketAddress.toString();
        JedisUtils.createNode(jedis, key, value);
    }

    @Override
    public void close() {
        jedis.close();
    }
}