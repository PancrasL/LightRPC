package github.pancras.registry.redis.util;

import org.junit.Test;

import java.util.List;

import redis.clients.jedis.Jedis;

/**
 * @author PancrasL
 */
public class JedisUtilsTest {
    Jedis jedis;
    boolean redisServerStarted = true;

    @Test
    public void testCreateAndGet() {
        if (!redisServerStarted)
            return;
        jedis = JedisUtils.getRedisClient();

        JedisUtils.createNode(jedis, "test", "test1");
        JedisUtils.createNode(jedis, "test", "test2");
        JedisUtils.createNode(jedis, "test", "test3");
        JedisUtils.createNode(jedis, "test", "test3");

        List<String> list = JedisUtils.getNodes(jedis, "test");
        System.out.println(list);

        JedisUtils.clearAll(jedis, "test");
        System.out.println(JedisUtils.getNodes(jedis, "test"));
    }
}