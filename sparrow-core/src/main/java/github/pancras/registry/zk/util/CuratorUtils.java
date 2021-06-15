package github.pancras.registry.zk.util;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import github.pancras.config.ServerConfig;

/**
 * @author pancras
 * @create 2021/6/10 10:54
 */
public class CuratorUtils {
    public static final String ZK_REGISTER_ROOT_PATH = "/sparrow-rpc";
    private static final Logger LOGGER = LoggerFactory.getLogger(CuratorUtils.class);
    private static final Set<String> REGISTERED_PATH_SET = ConcurrentHashMap.newKeySet();
    private static CuratorFramework zkClient;

    public static CuratorFramework getZkClient() {
        // if zkClient has been started, return directly
        if (zkClient != null && zkClient.getState() == CuratorFrameworkState.STARTED) {
            return zkClient;
        }
        // Retry strategy, retry 3 times
        RetryPolicy retryPolicy = new RetryNTimes(3, 1);
        String zkAddress = ServerConfig.ZK_ADDRESS;
        zkClient = CuratorFrameworkFactory.newClient(zkAddress, retryPolicy);
        zkClient.start();
        // Wait 5s until connect to zookeeper
        try {
            zkClient.blockUntilConnected(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return zkClient;
    }

    public static void createPersistentNode(CuratorFramework zkClient, String path) {
        try {
            if (REGISTERED_PATH_SET.contains(path) || zkClient.checkExists().forPath(path) != null) {
                LOGGER.warn("ZNode [{}] already exists", path);
            } else {
                zkClient.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path);
                LOGGER.info("ZNode [{}] was created successfully", path);
            }
            REGISTERED_PATH_SET.add(path);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static List<String> getChildrenNodes(CuratorFramework zkClient, String path) {
        try {
            return zkClient.getChildren().forPath(path);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static void deleteNode(CuratorFramework zkClient, String path) {
        try {
            zkClient.delete().deletingChildrenIfNeeded().forPath(path);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
