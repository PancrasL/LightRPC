package github.pancras.remoting.transport.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import github.pancras.commons.factory.SingletonFactory;
import github.pancras.remoting.dto.RpcRequest;
import github.pancras.remoting.dto.RpcResponse;
import github.pancras.remoting.handler.RpcRequestHandler;

/**
 * @author PancrasL
 */
public class SocketRpcServerHandler implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(SocketRpcServerHandler.class);

    private final Socket socket;
    private final RpcRequestHandler rpcRequestHandler;

    public SocketRpcServerHandler(Socket socket) {
        this.socket = socket;
        this.rpcRequestHandler = SingletonFactory.getInstance(RpcRequestHandler.class);
    }

    @Override
    public void run() {
        LOGGER.info("Thread [{}]: server handle message from client", Thread.currentThread());
        try (ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {
            RpcRequest rpcRequest = (RpcRequest) in.readObject();
            Object result = rpcRequestHandler.handle(rpcRequest);
            out.writeObject(RpcResponse.success(result, rpcRequest.getRequestId()));
            out.flush();
        } catch (IOException | ClassNotFoundException e) {
            LOGGER.error("Occur exception: ", e);
        }
    }
}
