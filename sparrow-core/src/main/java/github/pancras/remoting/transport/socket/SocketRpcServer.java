package github.pancras.remoting.transport.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import github.pancras.config.RpcServiceConfig;
import github.pancras.config.ServerConfig;
import github.pancras.factory.SingletonFactory;
import github.pancras.provider.ServiceProvider;
import github.pancras.provider.impl.ZkServiceProviderImpl;

/**
 * @author pancras
 * @create 2021/6/3 20:02
 */
public class SocketRpcServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(SocketRpcServer.class);

    private final ExecutorService threadPool;
    private final ServiceProvider serviceProvider;

    public SocketRpcServer() {
        threadPool = Executors.newCachedThreadPool();
        serviceProvider = SingletonFactory.getInstance(ZkServiceProviderImpl.class);
    }

    public void registerService(RpcServiceConfig rpcServiceConfig) {
        serviceProvider.publishService(rpcServiceConfig);
    }

    public void start() {
        try (ServerSocket server = new ServerSocket()) {
            String host = ServerConfig.SERVER_ADDRESS;
            int port = ServerConfig.PORT;
            InetSocketAddress inetSocketAddress = new InetSocketAddress(host, port);
            server.bind(inetSocketAddress);
            LOGGER.info("RPC Server listen at: [{}]", inetSocketAddress);
            Socket socket;
            while ((socket = server.accept()) != null) {
                LOGGER.info("RPC Client connected [{}]", socket.getInetAddress());
                threadPool.execute(new SocketRpcRequestHandler(socket));
            }
            threadPool.shutdown();
        } catch (IOException e) {
            LOGGER.error("RPC ServerSocket occur IOException:", e);
        }
    }
}
