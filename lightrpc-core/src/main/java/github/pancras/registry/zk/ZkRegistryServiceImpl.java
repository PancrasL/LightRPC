package github.pancras.registry.zk;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;

import github.pancras.commons.utils.NetUtil;
import github.pancras.registry.RegistryService;

/**
 * zookeeper path is /LightRPC/
 *
 * @author PancrasL
 */
@ThreadSafe
public class ZkRegistryServiceImpl implements RegistryService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZkRegistryServiceImpl.class);
    private static final Set<String> REGISTERED_PATH_SET = ConcurrentHashMap.newKeySet();
    private static final ConcurrentHashMap<String, List<String>> SERVICE_ADDRESS_MAP = new ConcurrentHashMap<>();
    private static final String ZK_REGISTER_ROOT_PATH = "/LightRPC/";
    private static final String ZK_PATH_SPLIT_CHAR = "/";
    private static final int DEFAULT_SESSION_TIMEOUT = 6000;
    private static final int DEFAULT_CONNECT_TIMEOUT = 2000;

    private final CuratorFramework zkClient;

    private ZkRegistryServiceImpl(InetSocketAddress address) {
        zkClient = buildZkClient(address);
    }

    public static ZkRegistryServiceImpl newInstance(InetSocketAddress address) {
        return new ZkRegistryServiceImpl(address);
    }

    @Override
    public void register(@Nonnull String rpcServiceName, @Nonnull InetSocketAddress address, @Nonnull Integer weight) throws Exception {
        NetUtil.validAddress(address);

        String path = getRegisterPath(rpcServiceName, address, weight);
        doRegister(path);
    }

    @Override
    public List<String> lookup(@Nonnull String rpcServiceName) {
        String path = ZK_REGISTER_ROOT_PATH + rpcServiceName;
        if (!SERVICE_ADDRESS_MAP.containsKey(path)) {
            List<String> serviceUrls = new ArrayList<>(0);
            try {
                serviceUrls = zkClient.getChildren().forPath(path);
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
            }
            SERVICE_ADDRESS_MAP.put(path, serviceUrls);
        }
        return SERVICE_ADDRESS_MAP.get(path);
    }

    @Override
    public void close() {
        REGISTERED_PATH_SET.clear();
        SERVICE_ADDRESS_MAP.clear();
        zkClient.close();
    }

    private void doRegister(String path) throws Exception {
        if (checkExists(path)) {
            LOGGER.warn("Fail: ZNode already existed [{}]", path);
            return;
        }
        // 创建临时节点，断开连接后会自动删除
        zkClient.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path);
        LOGGER.info("Success: Ephemeral ZNode [{}] was created", path);
        REGISTERED_PATH_SET.add(path);
    }

    private CuratorFramework buildZkClient(InetSocketAddress address) {
        CuratorFramework zkClient;
        // 重试3次，每次阻塞3s来连接Zookeeper
        RetryPolicy retryPolicy = new RetryNTimes(3, 1);
        String connectString = address.getHostString() + ":" + address.getPort();
        zkClient = CuratorFrameworkFactory.newClient(connectString, ZkRegistryServiceImpl.DEFAULT_SESSION_TIMEOUT, ZkRegistryServiceImpl.DEFAULT_CONNECT_TIMEOUT, retryPolicy);
        zkClient.start();
        try {
            zkClient.blockUntilConnected(3, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            LOGGER.error("Zookeeper connect fail.");
        }
        return zkClient;
    }

    private String getRegisterPath(String rpcServiceName, InetSocketAddress address, Integer weight) {
        return ZK_REGISTER_ROOT_PATH + rpcServiceName + ZK_PATH_SPLIT_CHAR + NetUtil.toStringAddress(address) + "@" + weight;
    }

    private boolean checkExists(String path) {
        try {
            if (REGISTERED_PATH_SET.contains(path)) {
                return true;
            } else if (zkClient.checkExists().forPath(path) != null) {
                REGISTERED_PATH_SET.add(path);
                return true;
            }
        } catch (Exception e) {
            LOGGER.error("ZNode " + path + " is not exist.");
        }
        return false;
    }
}
