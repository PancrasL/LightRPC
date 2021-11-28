package github.pancras.discovery;

import java.net.InetSocketAddress;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * 查询服务地址
 *
 * @author PancrasL
 */
public interface DiscoverService {
    /**
     * 根据配置的负载均衡策略返回服务地址
     *
     * @param rpcServiceName 服务名称
     * @return 服务地址 or null
     */
    @Nullable
    InetSocketAddress lookup(@Nonnull String rpcServiceName);

    /**
     * 释放所依赖的注册中心的资源
     */
    void close();
}
