package github.pancras.discover.loadbalance.loadbalancer;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import github.pancras.discover.loadbalance.LoadBalancer;

public class WeightRandomLoadBalancer implements LoadBalancer {
    private final ConcurrentHashMap<String, WeightRandomSelector> selectors = new ConcurrentHashMap<>();

    @Override
    public String selectAddress(List<String> rawAddresses, String rpcServiceName) {
        WeightRandomSelector selector = getSelector(rawAddresses, rpcServiceName);
        return selector.select();
    }

    private WeightRandomSelector getSelector(List<String> rawAddresses, String rpcServiceName) {
        WeightRandomSelector selector = selectors.get(rpcServiceName);
        // 如果服务列表更新了，或者selector不存在，需要创建selector
        if (selector == null || selector.identityHashCode != rawAddresses.hashCode()) {
            selectors.put(rpcServiceName, new WeightRandomSelector(rawAddresses, rawAddresses.hashCode()));
            selector = selectors.get(rpcServiceName);
        }

        return selector;
    }

    // LeetCode528
    static class WeightRandomSelector {
        // 地址列表
        private final List<String> rawAddresses;
        // 桩
        private final int[] piles;
        // 用于判断是否要更新Selector
        private final int identityHashCode;
        // 所有权重之和
        private int total;

        public WeightRandomSelector(List<String> rawAddresses, int identityHashCode) {
            this.rawAddresses = rawAddresses;
            int[] weight = new int[rawAddresses.size()];
            for (int i = 0; i < weight.length; i++) {
                String[] split = rawAddresses.get(i).split("@");
                weight[i] = Integer.parseInt(split[1]);
            }
            piles = new int[weight.length + 1];
            piles[0] = 0;
            for (int i = 0; i < weight.length; i++) {
                total += weight[i];
                piles[i + 1] = total;
            }
            this.identityHashCode = identityHashCode;
        }

        public String select() {
            int random = new Random().nextInt(total) + 1;
            int index = binarySearch(random);
            return rawAddresses.get(index).split("@")[0];
        }

        private int binarySearch(int target) {
            int left = 0;
            int right = piles.length - 1;
            while (left <= right) {
                int mid = left + (right - left) / 2;
                if (target == piles[mid]) {
                    return mid - 1;
                } else if (target < piles[mid]) {
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            }
            return left - 1;
        }
    }
}
