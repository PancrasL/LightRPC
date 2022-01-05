package github.pancras.provider.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.concurrent.ThreadSafe;

import github.pancras.commons.exception.RpcException;
import github.pancras.provider.ProviderService;
import github.pancras.registry.RegistryService;
import github.pancras.wrapper.RpcServiceConfig;

/**
 * @author PancrasL
 */
@ThreadSafe
public class ProviderServiceImpl implements ProviderService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProviderServiceImpl.class);

    private final ConcurrentHashMap<String, Object> serviceMap = new ConcurrentHashMap<>();
    private final RegistryService registry;

    private ProviderServiceImpl(RegistryService registry) {
        this.registry = registry;
    }

    public static ProviderServiceImpl newInstance(RegistryService registry) {
        return new ProviderServiceImpl(registry);
    }

    @Override
    public void publishService(RpcServiceConfig<?> rpcServiceConfig, InetSocketAddress address) throws Exception {
        registry.register(rpcServiceConfig.getRpcServiceName(), address, rpcServiceConfig.getWeight());
        addService(rpcServiceConfig);
    }

    @Override
    public Object getServiceInstance(String rpcServiceName) {
        Object service = serviceMap.get(rpcServiceName);
        if (service == null) {
            throw new RpcException(String.format("Service %s not found", rpcServiceName));
        }
        return service;
    }

    @Override
    public void close() {
        serviceMap.clear();
        registry.close();
        LOGGER.info("DefaultProviderServiceImpl is closed.");
    }

    private void addService(RpcServiceConfig<?> rpcServiceConfig) {
        String rpcServiceName = rpcServiceConfig.getRpcServiceName();
        if (serviceMap.containsKey(rpcServiceName)) {
            LOGGER.warn("Service [{}] has been published already", rpcServiceName);
            return;
        }
        serviceMap.put(rpcServiceName, rpcServiceConfig.getService());
        LOGGER.info("Add service:[{}], interfaces: [{}]", rpcServiceName, rpcServiceConfig.getService().getClass().getInterfaces());
    }
}
