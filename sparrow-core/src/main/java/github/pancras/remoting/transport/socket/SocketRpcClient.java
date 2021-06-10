package github.pancras.remoting.transport.socket;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import github.pancras.registry.ServiceDiscovery;
import github.pancras.registry.zk.ZkServiceDiscoveryImpl;
import github.pancras.remoting.dto.RpcRequest;
import github.pancras.remoting.transport.RpcRequestTransport;

/**
 * @author pancras
 * @create 2021/6/9 14:05
 */
public class SocketRpcClient implements RpcRequestTransport {
    private final ServiceDiscovery serviceDiscovery;

    public SocketRpcClient() {
        this.serviceDiscovery = new ZkServiceDiscoveryImpl();
    }

    @Override
    public Object sendRpcRequest(RpcRequest rpcRequest) {
        InetSocketAddress inetSocketAddress = serviceDiscovery.lookupService(rpcRequest.getRpcServiceName());
        try (Socket socket = new Socket()) {
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
}
