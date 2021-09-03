package github.pancras.registry.zk;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import github.pancras.commons.utils.NetUtil;
import github.pancras.config.SparrowConfig;
import github.pancras.registry.RegistryService;

/**
 * @author PancrasL
 */
public class ZkRegistryServiceImpl implements RegistryService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZkRegistryServiceImpl.class);
    private static final Set<String> REGISTERED_PATH_SET = ConcurrentHashMap.newKeySet();
    private static final String ZK_REGISTER_ROOT_PATH = "/sparrow-rpc/";
    private static final String ZK_PATH_SPLIT_CHAR = "/";
    private static final int DEFAULT_SESSION_TIMEOUT = 6000;
    private static final int DEFAULT_CONNECT_TIMEOUT = 2000;
    private static volatile ZkRegistryServiceImpl instance;
    private static volatile CuratorFramework zkClient;

    static ZkRegistryServiceImpl getInstance() {
        if (instance == null) {
            synchronized (ZkRegistryServiceImpl.class) {
                if (instance == null) {
                    instance = new ZkRegistryServiceImpl();
                }
            }
        }
        return instance;
    }

    @Override
    public void register(String rpcServiceName, InetSocketAddress address) throws Exception {
        NetUtil.validAddress(address);

        String path = getRegisterPath(rpcServiceName, address);
        doRegister(path);
    }

    private boolean doRegister(String path) {
        if (checkExists(path)) {
            LOGGER.info("Path already registered: [{}]", path);
            return true;
        }
        try {
            // 创建临时节点，断开连接后会自动删除
            getClientInstance().create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path);
            LOGGER.info("Ephemeral ZNode [{}] was created successfully", path);
            REGISTERED_PATH_SET.add(path);
            return true;
        } catch (Exception e) {
            LOGGER.error("Ephemeral ZNode [{}] created fail", path);
        }
        return false;
    }

    @Override
    public void unregister(String rpcServiceName, InetSocketAddress address) throws Exception {
        NetUtil.validAddress(address);

        String path = getRegisterPath(rpcServiceName, address);
        getClientInstance().delete().deletingChildrenIfNeeded().forPath(path);
        REGISTERED_PATH_SET.remove(path);
    }

    @Override
    public InetSocketAddress lookup(String rpcServiceName) throws Exception {
        if (rpcServiceName == null) {
            return null;
        }

        return doLookup(rpcServiceName);
    }

    private InetSocketAddress doLookup(String rpcSerivceName) {
        String path = ZK_REGISTER_ROOT_PATH + rpcSerivceName;
        List<String> serviceUrls = null;
        try {
            serviceUrls = getClientInstance().getChildren().forPath(path);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (null == serviceUrls || serviceUrls.isEmpty()) {
            throw new RuntimeException(String.format("Service %s not found", rpcSerivceName));
        }
        double randomNum = Math.random() * serviceUrls.size();
        String targetServiceUrl = serviceUrls.get((int) randomNum);
        LOGGER.debug("Get service: [{}]", targetServiceUrl);
        String[] hostPort = targetServiceUrl.split(":");
        return new InetSocketAddress(hostPort[0], Integer.parseInt(hostPort[1]));
    }

    @Override
    public void close() throws Exception {
        REGISTERED_PATH_SET.clear();
        getClientInstance().close();
    }

    private CuratorFramework getClientInstance() {
        if (zkClient == null) {
            synchronized (ZkRegistryServiceImpl.class) {
                if (zkClient == null) {
                    String address = SparrowConfig.DEFAULT_ZK_ADDRESS + ":" + SparrowConfig.DEFAULT_ZK_PORT;
                    zkClient = buildZkClient(address, DEFAULT_SESSION_TIMEOUT, DEFAULT_CONNECT_TIMEOUT);
                }
            }
        }
        return zkClient;
    }

    private CuratorFramework buildZkClient(String address, int sessionTimeoutMs, int connectTimeoutMs) {
        CuratorFramework zkClient;
        // 重试3次，每次阻塞3s来连接Zookeeper
        RetryPolicy retryPolicy = new RetryNTimes(3, 1);
        zkClient = CuratorFrameworkFactory.newClient(address, sessionTimeoutMs, connectTimeoutMs, retryPolicy);
        zkClient.start();
        try {
            zkClient.blockUntilConnected(3, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return zkClient;
    }

    private String getRegisterPath(String rpcServiceName, InetSocketAddress address) {
        return ZK_REGISTER_ROOT_PATH + rpcServiceName + ZK_PATH_SPLIT_CHAR + NetUtil.toStringAddress(address);
    }

    private boolean checkExists(String path) {
        try {
            if (REGISTERED_PATH_SET.contains(path)) {
                return true;
            } else if (getClientInstance().checkExists().forPath(path) != null) {
                REGISTERED_PATH_SET.add(path);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
