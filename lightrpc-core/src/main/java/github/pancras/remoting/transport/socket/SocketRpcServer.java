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
import github.pancras.provider.ProviderFactory;
import github.pancras.provider.ProviderService;
import github.pancras.remoting.transport.RpcServer;
import github.pancras.wrapper.RpcServiceConfig;

/**
 * @author PancrasL
 */
public class SocketRpcServer implements RpcServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(SocketRpcServer.class);

    private final InetSocketAddress address;
    private final ExecutorService threadPool;
    private final ProviderService providerService;

    private boolean isStarted = false;
    private ServerSocket server;

    public SocketRpcServer(InetSocketAddress address) {
        this.address = address;
        this.threadPool = Executors.newCachedThreadPool();
        this.providerService = ProviderFactory.getInstance();
    }

    @Override
    public void registerService(RpcServiceConfig<?> rpcServiceConfig) {
        try {
            providerService.publishService(rpcServiceConfig);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void start() throws Exception {
        if (isStarted) {
            throw new IllegalStateException("The server is already started, please do not start the service repeatedly.");
        }
        server = new ServerSocket();
        server.bind(address);
        isStarted = true;
        LOGGER.info("RPC Server listen at: [{}]", address);
        ShutdownHook.getInstance().addDisposable(this);

        Socket socket;
        while ((socket = server.accept()) != null) {
            LOGGER.info("RPC Client connected [{}]", socket.getInetAddress());
            threadPool.execute(new SocketRpcServerHandler(socket, providerService));
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
