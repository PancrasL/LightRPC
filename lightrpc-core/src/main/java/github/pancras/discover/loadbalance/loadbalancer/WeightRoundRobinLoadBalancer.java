package github.pancras.discover.loadbalance.loadbalancer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import github.pancras.discover.loadbalance.LoadBalancer;

public class WeightRoundRobinLoadBalancer implements LoadBalancer {
    private final Map<String, Integer> indexMap = new ConcurrentHashMap<>();

    @Override
    public String selectAddress(List<String> rawAddresses, String rpcServiceName) {
        List<String> addresses = processWeight(rawAddresses);
        if (!indexMap.containsKey(rpcServiceName)) {
            indexMap.put(rpcServiceName, 0);
        }
        int index = indexMap.get(rpcServiceName);
        String address = addresses.get(index % addresses.size());
        indexMap.put(rpcServiceName, index + 1);
        return address;
    }

    private List<String> processWeight(List<String> rawAddresses) {
        List<String> newAddressList = new ArrayList<>(rawAddresses.size());
        for (String weightAddress : rawAddresses) {
            String[] addressAndWeight = weightAddress.split("@");
            int cnt = Integer.parseInt(addressAndWeight[1]);
            for (int i = 0; i < cnt; i++) {
                newAddressList.add(addressAndWeight[0]);
            }
        }
        return newAddressList;
    }
}
