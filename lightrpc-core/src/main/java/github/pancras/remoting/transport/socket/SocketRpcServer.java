package github.pancras.remoting.transport.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Nonnull;

import github.pancras.commons.ShutdownHook;
import github.pancras.provider.ProviderService;
import github.pancras.provider.impl.DefaultProviderServiceImpl;
import github.pancras.registry.RegistryFactory;
import github.pancras.registry.RegistryService;
import github.pancras.remoting.transport.RpcServer;
import github.pancras.wrapper.RpcServiceConfig;
import io.netty.resolver.InetSocketAddressResolver;

/**
 * @author PancrasL
 */
public class SocketRpcServer implements RpcServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(SocketRpcServer.class);

    private final SocketAddress address;
    private final ExecutorService threadPool;
    private final ProviderService providerService;

    private boolean isStarted = false;
    private ServerSocket server;

    public SocketRpcServer(InetSocketAddress address) {
        this.address = address;
        this.threadPool = Executors.newCachedThreadPool();
        RegistryService registryService = RegistryFactory.getInstance();
        this.providerService = DefaultProviderServiceImpl.newInstance(registryService);
    }

    @Override
    public void registerService(@Nonnull RpcServiceConfig<?> rpcServiceConfig) throws Exception {
        providerService.publishService(rpcServiceConfig);
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

    @Override
    public void destroy() {
        try {
            isStarted = false;
            server.close();
            threadPool.shutdown();
        } catch (IOException ignored) {
            // app will exit
        }
    }
}
