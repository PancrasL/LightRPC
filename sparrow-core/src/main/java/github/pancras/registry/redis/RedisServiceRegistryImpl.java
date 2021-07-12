package github.pancras.registry.redis;

import java.net.InetSocketAddress;

import github.pancras.registry.ServiceRegistry;
import github.pancras.registry.redis.util.JedisUtils;
import redis.clients.jedis.Jedis;

/**
 * @author PancrasL
 */
public class RedisServiceRegistryImpl implements ServiceRegistry {
    @Override
    public void registerService(String rpcServiceName, InetSocketAddress inetSocketAddress) {
        String key = JedisUtils.REDIS_REGISTER_ROOT_PATH + "/" + rpcServiceName;
        String value = inetSocketAddress.toString();
        Jedis jedis = JedisUtils.getRedisClient();
        JedisUtils.createNode(jedis, key, value);
    }
}