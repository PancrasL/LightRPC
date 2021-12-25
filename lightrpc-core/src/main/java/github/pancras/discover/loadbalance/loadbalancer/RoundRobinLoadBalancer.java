package github.pancras.discover.loadbalance.loadbalancer;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import github.pancras.discover.loadbalance.LoadBalancer;

public class RoundRobinLoadBalancer implements LoadBalancer {
    private final Map<String, Integer> indexMap = new ConcurrentHashMap<>();

    @Override
    public String selectAddress(List<String> rawAddresses, String rpcServiceName) {
        if (!indexMap.containsKey(rpcServiceName)) {
            indexMap.put(rpcServiceName, 0);
        }
        int size = rawAddresses.size();
        int index = indexMap.get(rpcServiceName);
        String address = rawAddresses.get(index % size);
        indexMap.put(rpcServiceName, index + 1);
        return address.split("@")[0];
    }
}
