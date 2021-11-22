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

import javax.annotation.Nonnull;

import github.pancras.commons.utils.NetUtil;
import github.pancras.registry.RegistryService;

/**
 * @author PancrasL
 */
public class ZkRegistryServiceImpl implements RegistryService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZkRegistryServiceImpl.class);
    private static final Set<String> REGISTERED_PATH_SET = ConcurrentHashMap.newKeySet();
    private static final String ZK_REGISTER_ROOT_PATH = "/LightRPC/";
    private static final String ZK_PATH_SPLIT_CHAR = "/";
    private static final int DEFAULT_SESSION_TIMEOUT = 6000;
    private static final int DEFAULT_CONNECT_TIMEOUT = 2000;

    private final CuratorFramework zkClient;

    public static ZkRegistryServiceImpl newInstance(InetSocketAddress address) {
        return new ZkRegistryServiceImpl(address);
    }

    private ZkRegistryServiceImpl(InetSocketAddress address) {
        zkClient = buildZkClient(address);
    }

    @Override
    public void register(@Nonnull String rpcServiceName, @Nonnull InetSocketAddress address) throws Exception {
        NetUtil.validAddress(address);

        String path = getRegisterPath(rpcServiceName, address);
        doRegister(path);
    }

    @Override
    public void unregister(@Nonnull String rpcServiceName, @Nonnull InetSocketAddress address) {
        NetUtil.validAddress(address);

        String path = getRegisterPath(rpcServiceName, address);
        try {
            zkClient.delete().deletingChildrenIfNeeded().forPath(path);
        } catch (Exception e) {
            LOGGER.warn(String.format("Delete ZNode %s fail", path));
        }
        REGISTERED_PATH_SET.remove(path);
    }

    @Override
    public InetSocketAddress lookup(@Nonnull String rpcServiceName) throws Exception {
        return doLookup(rpcServiceName);
    }

    @Override
    public void close() {
        REGISTERED_PATH_SET.clear();
        zkClient.close();
    }

    private void doRegister(String path) throws Exception {
        if (checkExists(path)) {
            LOGGER.warn("Path already registered: [{}]", path);
            return;
        }
        // 创建临时节点，断开连接后会自动删除
        zkClient.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path);
        LOGGER.info("Ephemeral ZNode [{}] was created successfully", path);
        REGISTERED_PATH_SET.add(path);
    }

    private InetSocketAddress doLookup(String rpcSerivceName) throws Exception {
        String path = ZK_REGISTER_ROOT_PATH + rpcSerivceName;
        List<String> serviceUrls;
        serviceUrls = zkClient.getChildren().forPath(path);

        // TODO 利用SPI实现多种策略的负载均衡机制
        double randomNum = Math.random() * serviceUrls.size();
        String targetServiceUrl = serviceUrls.get((int) randomNum);
        LOGGER.debug("Get service: [{}]", targetServiceUrl);
        String[] hostPort = targetServiceUrl.split(":");
        return new InetSocketAddress(hostPort[0], Integer.parseInt(hostPort[1]));
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

    private String getRegisterPath(String rpcServiceName, InetSocketAddress address) {
        return ZK_REGISTER_ROOT_PATH + rpcServiceName + ZK_PATH_SPLIT_CHAR + NetUtil.toStringAddress(address);
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
