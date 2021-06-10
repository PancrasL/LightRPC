package github.pancras.registry.zk;

import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.List;

import github.pancras.registry.ServiceDiscovery;
import github.pancras.registry.zk.util.CuratorUtils;

/**
 * @author pancras
 * @create 2021/6/9 18:07
 */
public class ZkServiceDiscoveryImpl implements ServiceDiscovery {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZkServiceDiscoveryImpl.class);

    @Override
    public InetSocketAddress lookupService(String rpcSerivceName) {
        CuratorFramework zkClient = CuratorUtils.getZkClient();
        String path = CuratorUtils.ZK_REGISTER_ROOT_PATH + "/" + rpcSerivceName;
        List<String> serviceUrls = CuratorUtils.getChildrenNodes(zkClient, path);
        if (null == serviceUrls) {
            throw new RuntimeException(String.format("Service %s not found", rpcSerivceName));
        }
        double randomNum = Math.random() * serviceUrls.size();
        String targetServiceUrl = serviceUrls.get((int) randomNum);
        LOGGER.info("Get service: [{}]", targetServiceUrl);
        String[] hostPort = targetServiceUrl.split(":");
        return new InetSocketAddress(hostPort[0], Integer.parseInt(hostPort[1]));
    }
}
