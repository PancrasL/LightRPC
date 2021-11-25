package github.pancras.discovery;

import github.pancras.commons.utils.SpiServiceLoader;
import github.pancras.discovery.loadbalance.LoadBalancer;
import github.pancras.registry.RegistryService;
import java.net.InetSocketAddress;
import java.util.List;
import javax.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        List<InetSocketAddress> addresses = registry.lookup(rpcServiceName);
        if (addresses.isEmpty()) {
            return null;
        }
        return loadBalancer.selectAddress(addresses);
    }

    @Override
    public void close() {
        registry.close();
    }
}
