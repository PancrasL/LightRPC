package github.pancras.remoting.transport.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import github.pancras.commons.ShutdownHook;
import github.pancras.config.DefaultConfig;
import github.pancras.provider.ProviderFactory;
import github.pancras.wrapper.RpcServiceConfig;
import github.pancras.provider.ProviderService;
import github.pancras.remoting.transport.RpcServer;

/**
 * @author PancrasL
 */
public class SocketRpcServer implements RpcServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(SocketRpcServer.class);
    private final ExecutorService threadPool;
    private final ProviderService providerService;

    private boolean isStarted = false;
    private ServerSocket server;

    public SocketRpcServer() {
        threadPool = Executors.newCachedThreadPool();
        providerService = ProviderFactory.getInstance();
    }

    @Override
    public void registerService(RpcServiceConfig rpcServiceConfig) {
        try {
            providerService.publishService(rpcServiceConfig);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void start() throws Exception {
        start(DefaultConfig.DEFAULT_SERVER_ADDRESS, DefaultConfig.DEFAULT_SERVER_PORT);
    }

    @Override
    public void start(String host, int port) throws Exception {
        if (isStarted) {
            throw new IllegalStateException("The server is already started, please do not start the service repeatedly.");
        }
        server = new ServerSocket();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(host, port);
        server.bind(inetSocketAddress);
        isStarted = true;
        LOGGER.info("RPC Server listen at: [{}]", inetSocketAddress);
        ShutdownHook.getInstance().addDisposable(this);
        
        Socket socket;
        while ((socket = server.accept()) != null) {
            LOGGER.info("RPC Client connected [{}]", socket.getInetAddress());
            threadPool.execute(new SocketRpcServerHandler(socket));
        }
    }

    public void shutdown() {
        try {
            isStarted = false;
            server.close();
            threadPool.shutdown();
        } catch (IOException ignored) {
        }
    }

    @Override
    public void destroy() {
        this.shutdown();
    }
}
