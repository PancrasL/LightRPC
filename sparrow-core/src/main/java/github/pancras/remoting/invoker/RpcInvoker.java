package github.pancras.remoting.invoker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import github.pancras.commons.factory.SingletonFactory;
import github.pancras.provider.ServiceProvider;
import github.pancras.provider.impl.ServiceProviderImpl;
import github.pancras.remoting.dto.RpcRequest;

/**
 * @author pancras
 * @create 2021/6/5 19:13
 *
 * 利用反射机制在服务器端执行RPC方法
 */
public class RpcInvoker {
    private static final Logger LOGGER = LoggerFactory.getLogger(RpcInvoker.class);

    private final ServiceProvider serviceProvider;

    public RpcInvoker() {
        serviceProvider = SingletonFactory.getInstance(ServiceProviderImpl.class);
    }

    public Object handle(RpcRequest rpcRequest) {
        Object service = serviceProvider.getService(rpcRequest.getRpcServiceName());
        return invokeTargetMethod(rpcRequest, service);
    }

    private Object invokeTargetMethod(RpcRequest rpcRequest, Object service) {
        Object result;
        try {
            Method method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
            result = method.invoke(service, rpcRequest.getParameters());
            LOGGER.debug("service:[{}] successful invoke method:[{}]", rpcRequest.getRpcServiceName(), rpcRequest.getMethodName());
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return result;
    }
}
