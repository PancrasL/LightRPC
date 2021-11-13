package github.pancras.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

import github.pancras.remoting.dto.RpcRequest;
import github.pancras.remoting.dto.RpcResponse;
import github.pancras.remoting.transport.RpcClient;
import github.pancras.wrapper.RpcServiceConfig;

/**
 * @author PancrasL
 * <p>
 * 通过动态代理和反射机制隐藏数据传输细节
 */
public class RpcClientProxy implements InvocationHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(RpcClientProxy.class);

    private final RpcClient rpcClient;
    private final RpcServiceConfig rpcServiceConfig;

    public RpcClientProxy(RpcClient rpcClient, RpcServiceConfig rpcServiceConfig) {
        this.rpcClient = rpcClient;
        this.rpcServiceConfig = rpcServiceConfig;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        LOGGER.info("invoke method: [{}]", method.getName());
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setGroup(rpcServiceConfig.getGroup());
        rpcRequest.setVersion(rpcServiceConfig.getVersion());
        rpcRequest.setRequestId(UUID.randomUUID().toString());
        rpcRequest.setInterfaceName(method.getDeclaringClass().getName());
        rpcRequest.setMethodName(method.getName());
        rpcRequest.setParameters(args);
        rpcRequest.setParamTypes(method.getParameterTypes());

        RpcResponse<Object> rpcResponse = (RpcResponse<Object>) rpcClient.sendRpcRequest(rpcRequest);
        return rpcResponse.getData();
    }

    public <T> T getProxy(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, this);
    }
}
