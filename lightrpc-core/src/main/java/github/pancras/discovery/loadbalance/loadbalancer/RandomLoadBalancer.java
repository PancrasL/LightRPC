package github.pancras.discovery.loadbalance.loadbalancer;

import github.pancras.discovery.loadbalance.LoadBalancer;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Random;

/**
 * @author PancrasL
 */
public class RandomLoadBalancer implements LoadBalancer {
    @Override
    public InetSocketAddress selectAddress(List<InetSocketAddress> addressList) {
        Random random = new Random();
        return addressList.get(random.nextInt(addressList.size()));
    }
}
