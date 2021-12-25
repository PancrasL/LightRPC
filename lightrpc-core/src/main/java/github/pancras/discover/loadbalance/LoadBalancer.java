package github.pancras.discover.loadbalance;

import java.util.List;

public interface LoadBalancer {
    /**
     * 根据负载均衡策略从rawAddresses选出地址
     *
     * @param rawAddresses   包含权重信息的地址，例如权重为3的服务地址 127.0.0.1:7998@3
     * @param rpcServiceName 服务名称
     * @return 服务地址，例如127.0.0.1:7998
     */
    String selectAddress(List<String> rawAddresses, String rpcServiceName);
}
