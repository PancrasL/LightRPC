package github.pancras.registry.zk.util;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import github.pancras.config.SparrowConfig;

/**
 * @author pancras
 * @create 2021/6/10 10:54
 * <p>
 * Curator工具类，提供ZkClient的连接，ZNode的创建、获取、删除方法
 */
public class CuratorUtils {
    public static final String ZK_REGISTER_ROOT_PATH = "/sparrow-rpc";
    private static final Logger LOGGER = LoggerFactory.getLogger(CuratorUtils.class);
    private static final Set<String> REGISTERED_PATH_SET = ConcurrentHashMap.newKeySet();

    public static CuratorFramework getZkClient() {
        CuratorFramework zkClient;
        // 重试3次，每次阻塞3s来连接Zookeeper
        RetryPolicy retryPolicy = new RetryNTimes(3, 1);
        String zkAddress = SparrowConfig.DEFAULT_ZK_ADDRESS + ":" + SparrowConfig.DEFAULT_ZK_PORT;
        zkClient = CuratorFrameworkFactory.newClient(zkAddress, retryPolicy);
        zkClient.start();
        try {
            zkClient.blockUntilConnected(3, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return zkClient;
    }

    public static void createPersistentNode(CuratorFramework zkClient, String path) {
        try {
            if (REGISTERED_PATH_SET.contains(path) || zkClient.checkExists().forPath(path) != null) {
                LOGGER.warn("Persistent ZNode [{}] already exists", path);
            } else {
                zkClient.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path);
                LOGGER.info("Persistent ZNode [{}] was created successfully", path);
            }
            REGISTERED_PATH_SET.add(path);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static void createEphemeralNode(CuratorFramework zkClient, String path) {
        try {
            if (REGISTERED_PATH_SET.contains(path) || zkClient.checkExists().forPath(path) != null) {
                LOGGER.warn("Ephemeral ZNode [{}] already exists", path);
            } else {
                zkClient.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path);
                LOGGER.info(" Ephemeral ZNode [{}] was created successfully", path);
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
            REGISTERED_PATH_SET.remove(path);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
