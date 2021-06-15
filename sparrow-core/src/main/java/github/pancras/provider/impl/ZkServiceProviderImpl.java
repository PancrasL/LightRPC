package github.pancras.provider.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import github.pancras.config.RpcServiceConfig;
import github.pancras.config.ServerConfig;
import github.pancras.provider.ServiceProvider;
import github.pancras.registry.ServiceRegistry;
import github.pancras.registry.zk.ZkServiceRegistryImpl;

/**
 * @author pancras
 * @create 2021/6/6 15:40
 */
public class ZkServiceProviderImpl implements ServiceProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZkServiceProviderImpl.class);

    private final Map<String, Object> serviceMap;
    private final Set<String> registeredService;
    private final ServiceRegistry serviceRegistry;

    public ZkServiceProviderImpl() {
        serviceMap = new ConcurrentHashMap<>();
        registeredService = ConcurrentHashMap.newKeySet();
        serviceRegistry = new ZkServiceRegistryImpl();
    }

    @Override
    public void publishService(RpcServiceConfig rpcServiceConfig) {
        String host = ServerConfig.SERVICE_ADDRESS;
        int port = ServerConfig.PORT;
        this.addService(rpcServiceConfig);
        serviceRegistry.registerService(rpcServiceConfig.getRpcServiceName(), new InetSocketAddress(host, port));
    }

    @Override
    public void addService(RpcServiceConfig rpcServiceConfig) {
        String rpcServiceName = rpcServiceConfig.getRpcServiceName();
        if (registeredService.contains((rpcServiceName))) {
            return;
        }
        serviceMap.put(rpcServiceName, rpcServiceConfig.getService());
        LOGGER.info("Add service:[{}], interfaces: [{}] ", rpcServiceName, rpcServiceConfig.getService().getClass().getInterfaces());
    }

    @Override
    public Object getService(String rpcServiceName) {
        Object service = serviceMap.get(rpcServiceName);
        if (null == service) {
            throw new RuntimeException(String.format("RPC service [%s] can not be found", rpcServiceName));
        }
        return service;
    }
}
