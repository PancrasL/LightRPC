package github.pancras.remoting.transport.socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import github.pancras.remoting.dto.RpcRequest;
import github.pancras.remoting.transport.RpcRequestTransport;

/**
 * @author pancras
 * @create 2021/6/9 14:05
 */
public class SocketRpcClient implements RpcRequestTransport {

    @Override
    public Object sendRpcRequest(RpcRequest rpcRequest) {
        try (Socket socket = new Socket()) {
            InetSocketAddress inetSocketAddress = new InetSocketAddress(InetAddress.getLocalHost(), 7998);
            socket.connect(inetSocketAddress);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(rpcRequest);
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            Object obj = in.readObject();
            in.close();
            out.close();
            return obj;
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("RPC call fail:", e);
        }
    }
}
