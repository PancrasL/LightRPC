package github.pancras.remoting.transport.socket;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import github.pancras.registry.RegistryFactory;
import github.pancras.registry.RegistryService;
import github.pancras.remoting.dto.RpcRequest;
import github.pancras.remoting.transport.RpcClient;

/**
 * @author PancrasL
 */
public class SocketRpcClient implements RpcClient {
    private final RegistryService registryService;

    public SocketRpcClient() {
        this.registryService = RegistryFactory.getInstance();
    }

    @Override
    public Object sendRpcRequest(RpcRequest rpcRequest) {
        try (Socket socket = new Socket()) {
            InetSocketAddress inetSocketAddress = registryService.lookup(rpcRequest.getRpcServiceName());
            socket.connect(inetSocketAddress);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(rpcRequest);
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            Object obj = in.readObject();
            in.close();
            out.close();
            return obj;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void destroy() {
        try {
            registryService.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
