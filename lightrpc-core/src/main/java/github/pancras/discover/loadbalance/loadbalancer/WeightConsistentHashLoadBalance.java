package github.pancras.discover.loadbalance.loadbalancer;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import github.pancras.discover.loadbalance.LoadBalancer;

/**
 * 基于权重的一致性哈希，根据权重值为每个物理节点分配不同的虚拟节点，虚拟节点数 = 物理节点权重 * 基础虚拟节点（默认为100）。
 */
public class WeightConsistentHashLoadBalance implements LoadBalancer {
    private final ConcurrentHashMap<String, ConsistentHashSelector> selectors = new ConcurrentHashMap<>();

    @Override
    public String selectAddress(List<String> rawAddresses, String rpcServiceName) {
        ConsistentHashSelector selector = getSelector(rawAddresses, rpcServiceName);
        return selector.select(rpcServiceName);
    }

    private ConsistentHashSelector getSelector(List<String> rawAddresses, String rpcServiceName) {
        ConsistentHashSelector selector = selectors.get(rpcServiceName);
        // 如果服务列表更新了，或者selector不存在，需要创建selector
        if (selector == null || selector.identityHashCode != rawAddresses.hashCode()) {
            selectors.put(rpcServiceName, new ConsistentHashSelector(rawAddresses, rawAddresses.hashCode()));
            selector = selectors.get(rpcServiceName);
        }

        return selector;
    }

    static class ConsistentHashSelector {
        // 哈希环
        private final TreeMap<Long, String> circle;
        // 每一个物理节点所对应的虚拟节点的个数
        private final int virtualReplicas = 4;
        private final int identityHashCode;

        ConsistentHashSelector(List<String> addresses, int identityHashCode) {
            this.circle = new TreeMap<>();
            this.identityHashCode = identityHashCode;
            for (String address : addresses) {
                String[] weightAndAddress = address.split("@");
                add(weightAndAddress[0], Integer.parseInt(weightAndAddress[1]));
            }
        }

        public String select(String rpcServiceKey) {
            long hash = hash(rpcServiceKey);
            // 数据映射在两个虚拟节点之间，需要按照顺时针方向寻找机器
            if (!circle.containsKey(hash)) {
                SortedMap<Long, String> tailMap = circle.tailMap(hash);
                // 获得一个最近的顺时针节点
                hash = tailMap.isEmpty() ? circle.firstKey() : tailMap.firstKey();
            }
            return circle.get(hash);
        }

        private void add(String address, int weight) {
            // 对于一个实际机器节点 node, 对应 numberOfReplicas 个虚拟节点
            for (int i = 0; i < virtualReplicas * weight; i++) {
                circle.put(hash(address + i), address);
            }
        }

        /**
         * 实现一致性哈希算法中使用的哈希函数,使用MD5算法来保证一致性哈希的平衡性
         * hashCode不能达到很好的平衡性，因此重新设计了hash函数
         *
         * @param key the key
         * @return hash of the key,range = [0, 2^32-1]
         */
        private long hash(String key) {
            MessageDigest md5;
            try {
                md5 = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                throw new IllegalStateException("no md5 algrithm found");
            }
            md5.reset();
            md5.update(key.getBytes());
            byte[] bKey = md5.digest();
            //具体的哈希函数实现细节--每个字节 & 0xFF 再移位
            long result = ((long) (bKey[3] & 0xFF) << 24)
                    | ((long) (bKey[2] & 0xFF) << 16
                    | ((long) (bKey[1] & 0xFF) << 8) | (long) (bKey[0] & 0xFF));
            return result ^ 0xffffcd7d;
        }
    }
}
