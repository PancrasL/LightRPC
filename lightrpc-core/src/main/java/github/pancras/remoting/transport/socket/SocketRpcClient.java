package github.pancras.remoting.transport.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import javax.annotation.Nonnull;

import github.pancras.commons.ShutdownHook;
import github.pancras.discovery.DiscoverService;
import github.pancras.discovery.DiscoverServiceImpl;
import github.pancras.registry.RegistryFactory;
import github.pancras.registry.RegistryService;
import github.pancras.remoting.dto.RpcRequest;
import github.pancras.remoting.transport.RpcClient;
import github.pancras.wrapper.RegistryConfig;

/**
 * @author PancrasL
 */
public class SocketRpcClient implements RpcClient {
    private final static Logger LOGGER = LoggerFactory.getLogger(SocketRpcClient.class);
    private final DiscoverService discoverService;

    private SocketRpcClient(RegistryConfig registryConfig) {
        RegistryService registry = RegistryFactory.getRegistry(registryConfig);
        discoverService = DiscoverServiceImpl.getInstance(registry);
        ShutdownHook.getInstance().addDisposable(this);
    }

    public static SocketRpcClient getInstance(RegistryConfig registryConfig) {
        return new SocketRpcClient(registryConfig);
    }

    @Override
    public Object sendRpcRequest(@Nonnull RpcRequest rpcRequest) throws Exception {
        Socket socket = new Socket();
        // 重试3次连接，每次等待1s
        for (int i = 0; i < 3; i++) {
            InetSocketAddress address = discoverService.lookup(rpcRequest.getRpcServiceName());
            if (doConnect(socket, address)) {
                break;
            }
            Thread.sleep(1000);
            LOGGER.error("Connect to " + address + " fail, will try again.");
        }
        if (!socket.isConnected()) {
            throw new IllegalStateException("RpcRequest send fail: " + rpcRequest);
        }
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        out.writeObject(rpcRequest);
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
        Object obj = in.readObject();

        in.close();
        out.close();
        socket.close();
        return obj;
    }

    @Override
    public void destroy() {
        discoverService.close();
        LOGGER.info("SocketRpcClient is closed.");
    }

    private boolean doConnect(Socket socket, InetSocketAddress address) {
        try {
            socket.connect(address);
        } catch (IOException e) {
            LOGGER.error("Can not connect to " + address + ":" + e.getMessage());
            return false;
        }
        return true;
    }
}
