package github.pancras.discover;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.List;

import javax.annotation.Nonnull;

import github.pancras.commons.utils.SpiServiceLoader;
import github.pancras.discover.loadbalance.LoadBalancer;
import github.pancras.registry.RegistryService;

public class DiscoverServiceImpl implements DiscoverService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DiscoverServiceImpl.class);
    private final RegistryService registry;
    private final LoadBalancer loadBalancer;

    private DiscoverServiceImpl(RegistryService registry) {
        this.registry = registry;
        loadBalancer = SpiServiceLoader.loadService(LoadBalancer.class);
        LOGGER.info("Use loadbalancer: " + loadBalancer.getClass().getCanonicalName());
    }

    public static DiscoverServiceImpl getInstance(RegistryService registry) {
        return new DiscoverServiceImpl(registry);
    }

    @Override
    public InetSocketAddress lookup(@Nonnull String rpcServiceName) {
        List<String> addresses = registry.lookup(rpcServiceName);
        if (addresses.isEmpty()) {
            return null;
        }
        String url = loadBalancer.selectAddress(addresses, rpcServiceName);
        String[] ipAndPort = url.split(":");
        return new InetSocketAddress(ipAndPort[0], Integer.parseInt(ipAndPort[1]));
    }

    @Override
    public void close() {
        registry.close();
    }
}
