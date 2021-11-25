package github.pancras.discovery.loadbalance;

import java.net.InetSocketAddress;
import java.util.List;

public interface LoadBalancer {
    InetSocketAddress selectAddress(List<InetSocketAddress> addressList);
}
