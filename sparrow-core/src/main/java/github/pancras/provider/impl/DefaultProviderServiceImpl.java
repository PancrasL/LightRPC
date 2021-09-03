package github.pancras.provider.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import github.pancras.config.SparrowConfig;
import github.pancras.config.wrapper.RpcServiceConfig;
import github.pancras.provider.ProviderService;
import github.pancras.registry.RegistryFactory;
import github.pancras.registry.RegistryService;

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
    public void publishService(RpcServiceConfig rpcServiceConfig) {
        try {
            String host = SparrowConfig.SERVICE_REGISTER_ADDRESS;
            int port = SparrowConfig.DEFAULT_SERVER_PORT;
            registry.register(rpcServiceConfig.getRpcServiceName(), new InetSocketAddress(host, port));
            this.addService(rpcServiceConfig);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addService(RpcServiceConfig rpcServiceConfig) {
        String rpcServiceName = rpcServiceConfig.getRpcServiceName();
        if (registeredService.contains((rpcServiceName))) {
            LOGGER.warn("Service [{}] has been published already", rpcServiceName);
            return;
        }
        serviceMap.put(rpcServiceName, rpcServiceConfig.getService());
        LOGGER.info("Add service:[{}], interfaces: [{}]", rpcServiceName, rpcServiceConfig.getService().getClass().getInterfaces());
    }

    @Override
    public Object getServiceOrNull(String rpcServiceName) {
        return serviceMap.get(rpcServiceName);
    }
}
