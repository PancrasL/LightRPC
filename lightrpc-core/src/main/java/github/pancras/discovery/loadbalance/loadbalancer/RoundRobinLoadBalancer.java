package github.pancras.discovery.loadbalance.loadbalancer;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import github.pancras.discovery.loadbalance.LoadBalancer;

public class RoundRobinLoadBalancer implements LoadBalancer {
    private static final Map<String, Integer> indexMap = new ConcurrentHashMap<>();

    @Override
    public String selectAddress(List<String> addressList, String rpcServiceName) {
        if (!indexMap.containsKey(rpcServiceName)) {
            indexMap.put(rpcServiceName, 0);
        }
        int size = addressList.size();
        int index = indexMap.get(rpcServiceName);
        String address = addressList.get(index % size);
        indexMap.put(rpcServiceName, index);
        return address;
    }
}
