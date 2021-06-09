package github.pancras.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

import github.pancras.remoting.dto.RpcRequest;
import github.pancras.remoting.dto.RpcResponse;
import github.pancras.remoting.transport.RpcRequestTransport;
import github.pancras.remoting.transport.socket.SocketRpcClient;

/**
 * @author pancras
 * @create 2021/6/9 14:21
 */
public class RpcClientProxy implements InvocationHandler {
    private static final Logger logger = LoggerFactory.getLogger(RpcClientProxy.class);

    private final RpcRequestTransport rpcRequestTransport;

    public RpcClientProxy(RpcRequestTransport rpcRequestTransport) {
        this.rpcRequestTransport = rpcRequestTransport;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        logger.info("invoke method: [{}]", method.getName());
        RpcRequest rpcRequest = new RpcRequest(UUID.randomUUID().toString(), method.getDeclaringClass().getName(), method.getName(), args, method.getParameterTypes());
        RpcResponse<Object> rpcResponse = null;
        if (rpcRequestTransport instanceof SocketRpcClient) {
            rpcResponse = (RpcResponse<Object>) rpcRequestTransport.sendRpcRequest(rpcRequest);
        }
        assert rpcResponse != null;
        return rpcResponse.getData();
    }

    public <T> T getProxy(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, this);
    }
}
