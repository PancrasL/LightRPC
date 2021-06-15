package github.pancras.registry.zk;

import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

import github.pancras.registry.ServiceRegistry;
import github.pancras.registry.zk.util.CuratorUtils;

/**
 * @author pancras
 * @create 2021/6/9 18:07
 */
public class ZkServiceRegistryImpl implements ServiceRegistry {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZkServiceRegistryImpl.class);

    @Override
    public void registerService(String rpcServiceName, InetSocketAddress inetSocketAddress) {
        String servicePath = CuratorUtils.ZK_REGISTER_ROOT_PATH + "/" + rpcServiceName + inetSocketAddress.toString();
        CuratorFramework zkClient = CuratorUtils.getZkClient();
        try {
            CuratorUtils.createEphemeralNode(zkClient, servicePath);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
