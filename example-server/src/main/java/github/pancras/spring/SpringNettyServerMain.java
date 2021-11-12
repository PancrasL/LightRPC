package github.pancras.spring;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import github.pancras.HelloService;
import github.pancras.remoting.transport.netty.server.NettyRpcServer;
import github.pancras.serviceimpl.HelloServiceImpl;
import github.pancras.spring.annotation.RpcScan;
import github.pancras.wrapper.RpcServiceConfig;

@RpcScan(basePackage = "github.pancras")
public class SpringNettyServerMain {
    public static void main(String[] args) throws Exception {
        // Register service via annotation
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(SpringNettyServerMain.class);
        NettyRpcServer nettyRpcServer = (NettyRpcServer) applicationContext.getBean("nettyRpcServer");
        // Register service manually
        HelloService helloService2 = new HelloServiceImpl();
        RpcServiceConfig rpcServiceConfig = new RpcServiceConfig(helloService2);
        nettyRpcServer.registerService(rpcServiceConfig);
        nettyRpcServer.start();
    }
}
