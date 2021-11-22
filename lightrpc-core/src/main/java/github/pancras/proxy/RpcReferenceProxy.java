package github.pancras.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

import github.pancras.exception.RpcException;
import github.pancras.remoting.dto.RpcRequest;
import github.pancras.remoting.dto.RpcResponse;
import github.pancras.remoting.transport.RpcClient;
import github.pancras.wrapper.RpcReferenceConfig;

/**
 * @author PancrasL
 * <p>
 * 通过动态代理和反射机制隐藏数据传输细节
 */
public class RpcReferenceProxy<T> implements InvocationHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(RpcReferenceProxy.class);

    /**
     * RPC请求重试次数
     */
    private static final int RETRY_TIMES = 3;

    private final RpcClient rpcClient;
    private final RpcReferenceConfig<T> rpcReferenceConfig;

    private RpcReferenceProxy(RpcClient rpcClient, RpcReferenceConfig<T> referenceConfig) {
        this.rpcClient = rpcClient;
        this.rpcReferenceConfig = referenceConfig;
    }

    /**
     * 此方法返回一个代理对象
     *
     * @param referenceConfig 服务引用配置
     * @return 服务引用的代理类
     */
    public static <T> Object newProxyInstance(RpcReferenceConfig<T> referenceConfig) {
        RpcClient rpcClient = referenceConfig.getRpcClient();
        Class<T> interfac = referenceConfig.getInterface();
        return Proxy.newProxyInstance(interfac.getClassLoader(), new Class[]{interfac}, new RpcReferenceProxy<>(rpcClient, referenceConfig));
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws InterruptedException {
        LOGGER.debug("invoke method: [{}]", method.getName());
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setGroup(rpcReferenceConfig.getGroup());
        rpcRequest.setVersion(rpcReferenceConfig.getVersion());
        rpcRequest.setRequestId(UUID.randomUUID().toString());
        rpcRequest.setInterfaceName(method.getDeclaringClass().getName());
        rpcRequest.setMethodName(method.getName());
        rpcRequest.setParameters(args);
        rpcRequest.setParamTypes(method.getParameterTypes());

        Object response = null;
        for (int i = 0; i < RETRY_TIMES; i++) {
            response = send(rpcRequest);
            if (response != null) {
                break;
            }
            Thread.sleep(1000);
        }
        if (response == null) {
            throw new RpcException("RpcRequest " + rpcRequest + " send fail.");
        }

        if (response instanceof RpcResponse) {
            return ((RpcResponse<?>) response).getData();
        }
        throw new IllegalStateException("Bad response");
    }

    private Object send(RpcRequest request) {
        Object response = null;
        try {
            response = rpcClient.sendRpcRequest(request);
        } catch (Exception e) {
            LOGGER.error("RpcRequest: " + request + " send fail, will try again: " + e.getMessage());
            e.printStackTrace();
        }
        return response;
    }
}
