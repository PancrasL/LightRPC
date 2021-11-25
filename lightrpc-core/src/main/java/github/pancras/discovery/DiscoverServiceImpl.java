package github.pancras.discovery;

import java.net.InetSocketAddress;
import java.util.List;

import javax.annotation.Nonnull;

import github.pancras.registry.RegistryService;

/**
 * @author PancrasL
 */
public class DiscoverServiceImpl implements DiscoverService {
    private final RegistryService registry;

    private DiscoverServiceImpl(RegistryService registry) {
        this.registry = registry;
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
        return registry.lookup(rpcServiceName).get(0);
    }

    @Override
    public void close() {
        registry.close();
    }
}
