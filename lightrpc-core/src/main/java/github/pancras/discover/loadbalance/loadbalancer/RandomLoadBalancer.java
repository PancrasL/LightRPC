package github.pancras.discover.loadbalance.loadbalancer;

import java.util.List;
import java.util.Random;

import github.pancras.discover.loadbalance.LoadBalancer;

/**
 * @author PancrasL
 */
public class RandomLoadBalancer implements LoadBalancer {
    @Override
    public String selectAddress(List<String> rawAddresses, String rpcServiceName) {
        Random random = new Random();
        String address = rawAddresses.get(random.nextInt(rawAddresses.size()));
        // 忽略权重信息
        return address.split("@")[0];
    }
}
