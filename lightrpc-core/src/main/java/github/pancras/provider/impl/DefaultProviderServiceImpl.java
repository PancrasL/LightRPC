package github.pancras.provider.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import github.pancras.config.DefaultConfig;
import github.pancras.exception.RpcException;
import github.pancras.provider.ProviderService;
import github.pancras.registry.RegistryFactory;
import github.pancras.registry.RegistryService;
import github.pancras.wrapper.RpcServiceConfig;

/**
 * @author PancrasL
 */
public class DefaultProviderServiceImpl implements ProviderService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultProviderServiceImpl.class);

    private final Map<String, Object> serviceMap;
    private final Set<String> registeredService;
    private final RegistryService registry;

    public DefaultProviderServiceImpl() {
        serviceMap = new ConcurrentHashMap<>();
        registeredService = ConcurrentHashMap.newKeySet();
        registry = RegistryFactory.getInstance();
    }

    @Override
    public void publishService(RpcServiceConfig rpcServiceConfig) throws Exception {
        String host = DefaultConfig.SERVICE_REGISTER_ADDRESS;
        int port = DefaultConfig.DEFAULT_SERVER_PORT;
        registry.register(rpcServiceConfig.getRpcServiceName(), new InetSocketAddress(host, port));
        
        addServiceToCache(rpcServiceConfig);
    }

    private void addServiceToCache(RpcServiceConfig rpcServiceConfig) {
        String rpcServiceName = rpcServiceConfig.getRpcServiceName();
        if (registeredService.contains((rpcServiceName))) {
            LOGGER.warn("Service [{}] has been published already", rpcServiceName);
            return;
        }
        serviceMap.put(rpcServiceName, rpcServiceConfig.getService());
        LOGGER.info("Add service:[{}], interfaces: [{}]", rpcServiceName, rpcServiceConfig.getService().getClass().getInterfaces());
    }

    @Override
    public Object getService(String rpcServiceName) {
        Object service = serviceMap.get(rpcServiceName);
        if (service == null) {
            throw new RpcException(String.format("Service %s not found", rpcServiceName));
        }
        return service;
    }
}
