package github.pancras.registry.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.List;

import github.pancras.registry.RegistryService;
import github.pancras.registry.redis.util.JedisUtils;
import redis.clients.jedis.Jedis;

/**
 * @author PancrasL
 */
public class RedisRegistryServiceImpl implements RegistryService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisRegistryServiceImpl.class);

    Jedis jedis = JedisUtils.getRedisClient();

    @Override
    public void register(String rpcServiceName, InetSocketAddress address) {
        String key = JedisUtils.REDIS_REGISTER_ROOT_PATH + "/" + rpcServiceName;
        String value = address.toString();
        JedisUtils.createNode(jedis, key, value);
    }

    @Override
    public void unregister(String rpcServiceName, InetSocketAddress address){

    }

    @Override
    public InetSocketAddress lookup(String rpcServiceName) {
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
}
