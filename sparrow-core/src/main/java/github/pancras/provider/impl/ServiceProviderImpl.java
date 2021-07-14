package github.pancras.provider.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import github.pancras.commons.factory.RegistryFactory;
import github.pancras.config.RpcServiceConfig;
import github.pancras.config.SparrowConfig;
import github.pancras.provider.ServiceProvider;
import github.pancras.registry.ServiceRegistry;

/**
 * @author pancras
 * @create 2021/6/6 15:40
 */
public class ServiceProviderImpl implements ServiceProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceProviderImpl.class);

    private final Map<String, Object> serviceMap;
    private final Set<String> registeredService;
    private final ServiceRegistry serviceRegistry;

    public ServiceProviderImpl() {
        serviceMap = new ConcurrentHashMap<>();
        registeredService = ConcurrentHashMap.newKeySet();
        serviceRegistry = RegistryFactory.getRegistry(SparrowConfig.DEFAULT_REGISRY_TYPE);
    }

    @Override
    public void publishService(RpcServiceConfig rpcServiceConfig) {
        String host = SparrowConfig.SERVICE_REGISTER_ADDRESS;
        int port = SparrowConfig.DEFAULT_SERVER_PORT;
        this.addService(rpcServiceConfig);
        serviceRegistry.registerService(rpcServiceConfig.getRpcServiceName(), new InetSocketAddress(host, port));
    }

    @Override
    public void addService(RpcServiceConfig rpcServiceConfig) {
        String rpcServiceName = rpcServiceConfig.getRpcServiceName();
        if (registeredService.contains((rpcServiceName))) {
            LOGGER.warn("Service [{}] has been published already", rpcServiceName);
            return;
        }
        serviceMap.put(rpcServiceName, rpcServiceConfig.getService());
        LOGGER.info("Add service:[{}], interfaces: [{}] to local cache", rpcServiceName, rpcServiceConfig.getService().getClass().getInterfaces());
    }

    @Override
    public Object getService(String rpcServiceName) {
        Object service = serviceMap.get(rpcServiceName);
        if (null == service) {
            throw new RuntimeException(String.format("RPC service [%s] can not be found", rpcServiceName));
        }
        return service;
    }

    @Override
    public void close() throws IOException {
        serviceMap.clear();
        registeredService.clear();
        serviceRegistry.close();
    }
}
