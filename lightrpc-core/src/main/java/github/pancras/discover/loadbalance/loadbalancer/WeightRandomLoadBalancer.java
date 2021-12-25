package github.pancras.discover.loadbalance.loadbalancer;

import java.util.List;
import java.util.Random;

import github.pancras.discover.loadbalance.LoadBalancer;

public class WeightRandomLoadBalancer implements LoadBalancer {
    @Override
    public String selectAddress(List<String> rawAddresses, String rpcServiceName) {
        List<String> addresses = LoadBalancerUtil.processWeight(rawAddresses);
        Random random = new Random();
        return addresses.get(random.nextInt(addresses.size()));
    }
}
