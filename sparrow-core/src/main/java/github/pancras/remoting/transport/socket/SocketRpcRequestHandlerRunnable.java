package github.pancras.remoting.transport.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import github.pancras.factory.SingletonFactory;
import github.pancras.remoting.dto.RpcRequest;
import github.pancras.remoting.dto.RpcResponse;
import github.pancras.remoting.handler.RpcRequestHandler;

/**
 * @author pancras
 * @create 2021/6/5 18:59
 */
// TODO rename to SocketRpcRequestHandler?
public class SocketRpcRequestHandlerRunnable implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(SocketRpcRequestHandlerRunnable.class);

    private final Socket socket;
    private final RpcRequestHandler rpcRequestHandler;

    public SocketRpcRequestHandlerRunnable(Socket socket) {
        this.socket = socket;
        this.rpcRequestHandler = SingletonFactory.getInstance(RpcRequestHandler.class);
    }

    @Override
    public void run() {
        logger.info("Thread [{}]: server handle message from client", Thread.currentThread());
        try (ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {
            RpcRequest rpcRequest = (RpcRequest) in.readObject();
            Object result = rpcRequestHandler.handle(rpcRequest);
            out.writeObject(RpcResponse.success(result, rpcRequest.getRequestId()));
            out.flush();
        } catch (IOException | ClassNotFoundException e) {
            logger.error("Occur exception: ", e);
        }
    }
}
