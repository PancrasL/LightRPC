package github.pancras.discover.loadbalance;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import github.pancras.discover.loadbalance.loadbalancer.RandomLoadBalancer;
import github.pancras.discover.loadbalance.loadbalancer.RoundRobinLoadBalancer;
import github.pancras.discover.loadbalance.loadbalancer.WeightConsistentHashLoadBalance;
import github.pancras.discover.loadbalance.loadbalancer.WeightRandomLoadBalancer;
import github.pancras.discover.loadbalance.loadbalancer.WeightRoundRobinLoadBalancer;

public class LoadBalancerTest {
    private final List<String> fakeAddresses = new ArrayList<>();

    {
        fakeAddresses.add("1.1.1.1:7998@1");
        fakeAddresses.add("2.2.2.2:7998@2");
        fakeAddresses.add("3.3.3.3:7998@3");
        fakeAddresses.add("4.4.4.4:7998@4");
    }

    @Test
    public void testRandom() {
        System.out.println("随机负载均衡：");
        LoadBalancer loadBalancer = new RandomLoadBalancer();
        for (int i = 0; i < 10; i++) {
            String address = loadBalancer.selectAddress(fakeAddresses, "ignore");
            System.out.println(address);
        }
    }

    @Test
    public void testWeightRandom() {
        System.out.println("权重随机负载均衡：");
        LoadBalancer loadBalancer = new WeightRandomLoadBalancer();
        for (int i = 0; i < 10; i++) {
            String address = loadBalancer.selectAddress(fakeAddresses, "ignore");
            System.out.println(address);
        }
    }

    @Test
    public void testRoundRobin() {
        System.out.println("轮询负载均衡：");
        LoadBalancer loadBalancer = new RoundRobinLoadBalancer();
        for (int i = 0; i < 10; i++) {
            String address = loadBalancer.selectAddress(fakeAddresses, "ignore");
            System.out.println(address);
        }
    }

    @Test
    public void testWeightRoundRobin() {
        System.out.println("权重轮询负载均衡：");
        LoadBalancer loadBalancer = new WeightRoundRobinLoadBalancer();
        for (int i = 0; i < 15; i++) {
            String address = loadBalancer.selectAddress(fakeAddresses, "ignore");
            System.out.println(address);
        }
    }

    @Test
    public void testConsistentHash() {
        System.out.println("一致性哈希负载均衡：");
        LoadBalancer loadBalancer = new WeightConsistentHashLoadBalance();
        for (int i = 0; i < 10; i++) {
            String address = loadBalancer.selectAddress(fakeAddresses, "" + i % 3);
            System.out.println(address);
        }
        System.out.println("插入新节点");
        fakeAddresses.add("8.8.8.8:232@4");
        for (int i = 0; i < 10; i++) {
            String address = loadBalancer.selectAddress(fakeAddresses, "" + i % 3);
            System.out.println(address);
        }
    }
}