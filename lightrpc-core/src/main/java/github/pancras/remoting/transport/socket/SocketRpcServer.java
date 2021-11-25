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
import github.pancras.wrapper.RegistryConfig;
import github.pancras.wrapper.RpcServiceConfig;

/**
 * @author PancrasL
 */
public class SocketRpcServer implements RpcServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(SocketRpcServer.class);

    private final SocketAddress address;
    private final ExecutorService threadPool;
    private final ProviderService providerService;

    private ServerSocket server;

    private SocketRpcServer(InetSocketAddress address, RegistryConfig registryConfig) {
        this.address = address;
        this.threadPool = Executors.newCachedThreadPool();
        RegistryService registryService = RegistryFactory.getRegistry(registryConfig);
        this.providerService = DefaultProviderServiceImpl.newInstance(registryService);
    }

    public static SocketRpcServer getInstance(@Nonnull InetSocketAddress address, @Nonnull RegistryConfig registryConfig) {
        return new SocketRpcServer(address, registryConfig);
    }

    @Override
    public void registerService(@Nonnull RpcServiceConfig<?> rpcServiceConfig) throws Exception {
        providerService.publishService(rpcServiceConfig);
    }

    @Override
    public void start() throws Exception {
        server = new ServerSocket();
        server.bind(address);
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
            server.close();
            threadPool.shutdown();
        } catch (IOException ignored) {
            // app will exit
        }
    }
}
