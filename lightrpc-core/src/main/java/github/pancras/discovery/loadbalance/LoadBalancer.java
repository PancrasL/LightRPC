package github.pancras.discovery.loadbalance;

import java.util.List;

public interface LoadBalancer {
    String selectAddress(List<String> addressList, String rpcServiceName);
}
