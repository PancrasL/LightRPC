package github.pancras.registry.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;

import github.pancras.registry.RegistryService;
import redis.clients.jedis.Jedis;

/**
 * @author PancrasL
 */
public class RedisRegistryServiceImpl implements RegistryService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisRegistryServiceImpl.class);
    public static final String REDIS_REGISTER_ROOT_PATH = "/LightRPC";
    private static final ConcurrentHashMap<String, List<String>> SERVICE_ADDRESS_MAP = new ConcurrentHashMap<>();
    private final ScheduledExecutorService pullService = Executors.newSingleThreadScheduledExecutor();

    private final Jedis jedis;

    private RedisRegistryServiceImpl(InetSocketAddress socketAddress) {
        this.jedis = buildJedis(socketAddress);
        // 每30秒更新一次服务地址列表
        pullService.schedule(() -> SERVICE_ADDRESS_MAP.forEachEntry(5, (e) -> {
            ArrayList<String> address = new ArrayList<>(jedis.smembers(e.getKey()));
            e.setValue(address);
        }), 30, TimeUnit.SECONDS);
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
        String key = REDIS_REGISTER_ROOT_PATH + "/" + rpcServiceName;
        String value = address.toString();
        jedis.sadd(key, value);
    }

    @Override
    public List<String> lookup(@Nonnull String rpcServiceName) {
        String key = REDIS_REGISTER_ROOT_PATH + "/" + rpcServiceName;
        if (!SERVICE_ADDRESS_MAP.containsKey(key)) {
            ArrayList<String> address = new ArrayList<>(jedis.smembers(key));
            SERVICE_ADDRESS_MAP.put(key, address);
        }
        return SERVICE_ADDRESS_MAP.get(key);
    }

    @Override
    public void close() {
        jedis.close();
    }
}
