package github.pancras.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import github.pancras.remoting.transport.netty.server.NettyRpcServer;
import github.pancras.spring.annotation.RpcScan;

/**
 * @author PancrasL
 */
@RpcScan(basePackage = {"github.pancras.spring"})
public class NettyServerApplication {
    public static void main(String[] args) throws Exception {
        ApplicationContext context = new AnnotationConfigApplicationContext(NettyServerApplication.class);
        NettyRpcServer nettyRpcServer = context.getBean(NettyRpcServer.class);
        nettyRpcServer.start();
    }
}
