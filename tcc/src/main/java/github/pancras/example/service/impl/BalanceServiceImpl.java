package github.pancras.example.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.InetSocketAddress;

import github.pancras.example.service.BalanceService;
import github.pancras.remoting.transport.RpcServer;
import github.pancras.remoting.transport.netty.server.NettyRpcServer;
import github.pancras.tcc.annotation.LocalTcc;
import github.pancras.wrapper.RegistryConfig;
import github.pancras.wrapper.RpcServiceConfig;

@Service
@LocalTcc
public class BalanceServiceImpl implements BalanceService {
    private static final Logger LOGGER = LoggerFactory.getLogger(BalanceServiceImpl.class);

    public static void main(String[] args) throws Exception {
        // 创建服务实例包装类
        BalanceServiceImpl service = new BalanceServiceImpl();
        RpcServiceConfig<BalanceServiceImpl> config = RpcServiceConfig
                .newDefaultConfig(service);
        // 创建服务器
        InetSocketAddress address = new InetSocketAddress("127.0.0.1", 7990);
        RpcServer server = NettyRpcServer.getInstance(address, RegistryConfig.newConfig("zookeeper", "127.0.0.1", 2181));
        // 发布服务
        server.registerService(config);
        // 启动服务器
        server.start();
    }

    @Override
    public void balanceTry() {
        LOGGER.info("try");
    }

    @Override
    public void confirm() {
        LOGGER.info("confirm");
    }

    @Override
    public void cancel() {
        LOGGER.info("cancel");
    }

}
