package github.pancras.provider.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import github.pancras.config.RpcServiceConfig;
import github.pancras.provider.ServiceProvider;

/**
 * @author pancras
 * @create 2021/6/6 15:40
 */
public class ServiceProviderImpl implements ServiceProvider {
    private static final Logger logger = LoggerFactory.getLogger(ServiceProviderImpl.class);

    private final Map<String, Object> serviceMap;
    private final Set<String> registeredService;

    public ServiceProviderImpl() {
        serviceMap = new ConcurrentHashMap<>();
        registeredService = ConcurrentHashMap.newKeySet();
    }

    @Override
    public void addService(RpcServiceConfig rpcServiceConfig) {
        String rpcServiceName = rpcServiceConfig.getRpcServiceName();
        if (registeredService.contains((rpcServiceName))) {
            return;
        }
        serviceMap.put(rpcServiceName, rpcServiceConfig.getService());
        // TODO add interfaces log: rpcServiceConfig.getService().getClass().getInterfaces()
        logger.info("Add Service:{}", rpcServiceName);
    }

    @Override
    public Object getService(String rpcServiceName) {
        Object service = serviceMap.get(rpcServiceName);
        if (null == service) {
            throw new RuntimeException("Service can not be found");
        }
        return service;
    }
}
