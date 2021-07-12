package github.pancras.registry.redis.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import github.pancras.config.SparrowConfig;
import redis.clients.jedis.Jedis;

/**
 * @author PancrasL
 */
public class JedisUtils {
    public static final String REDIS_REGISTER_ROOT_PATH = "/sparrow-rpc";
    private static final Logger LOGGER = LoggerFactory.getLogger(JedisUtils.class);
    private static Jedis jedis;

    public static Jedis getRedisClient() {
        // 如果jedis已经连接，直接返回
        if (jedis != null && jedis.isConnected()) {
            return jedis;
        }
        // 连接redis，最多等待3s
        jedis = new Jedis(SparrowConfig.DEFAULT_REDIS_ADDRESS, SparrowConfig.DEFAULT_REDIS_PORT, 3000);
        jedis.connect();
        return jedis;
    }

    public static void createNode(Jedis jedis, String key, String value) {
        jedis.sadd(key, value);
    }

    public static List<String> getNodes(Jedis jedis, String key) {
        return new ArrayList<>(jedis.smembers(key));
    }

    public static void clearAll(Jedis jedis, String key) {
        jedis.del(key);
    }
}