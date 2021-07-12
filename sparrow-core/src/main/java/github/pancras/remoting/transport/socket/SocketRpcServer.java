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
import github.pancras.config.SparrowConfig;
import github.pancras.provider.ServiceProvider;
import github.pancras.provider.impl.ServiceProviderImpl;
import github.pancras.remoting.transport.RpcServer;

/**
 * @author pancras
 * @create 2021/6/3 20:02
 */
public class SocketRpcServer implements RpcServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(SocketRpcServer.class);
    private final ExecutorService threadPool;
    private final ServiceProvider serviceProvider;

    private ServerSocket server;

    public SocketRpcServer() {
        threadPool = Executors.newCachedThreadPool();
        serviceProvider = new ServiceProviderImpl();
    }

    @Override
    public void registerService(RpcServiceConfig rpcServiceConfig) {
        serviceProvider.publishService(rpcServiceConfig);
    }

    @Override
    public void start() throws Exception {
        server = new ServerSocket();
        String host = SparrowConfig.SERVER_LISTEN_ADDRESS;
        int port = SparrowConfig.PORT;
        InetSocketAddress inetSocketAddress = new InetSocketAddress(host, port);
        server.bind(inetSocketAddress);
        LOGGER.info("RPC Server listen at: [{}]", inetSocketAddress);
        Socket socket;
        while ((socket = server.accept()) != null) {
            LOGGER.info("RPC Client connected [{}]", socket.getInetAddress());
            threadPool.execute(new SocketRpcServerHandler(socket));
        }
        threadPool.shutdown();
    }

    @Override
    public void close() {
        try {
            server.close();
            serviceProvider.close();
            threadPool.shutdown();
        } catch (IOException ignored) {
        }
    }
}
