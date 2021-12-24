package github.pancras.discovery.loadbalance.loadbalancer;

import java.util.List;
import java.util.Random;

import github.pancras.discovery.loadbalance.LoadBalancer;

/**
 * @author PancrasL
 */
public class RandomLoadBalancer implements LoadBalancer {
    @Override
    public String selectAddress(List<String> addressList, String rpcServiceName) {
        Random random = new Random();
        return addressList.get(random.nextInt(addressList.size()));
    }
}
