package github.pancras.remoting.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import github.pancras.provider.ProviderFactory;
import github.pancras.provider.ProviderService;
import github.pancras.remoting.dto.RpcRequest;

/**
 * @author PancrasL
 * <p>
 * 获取到服务对象，并利用反射机制在服务器端执行RPC方法
 */
public class RpcRequestHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(RpcRequestHandler.class);

    private final ProviderService providerService;

    public RpcRequestHandler() {
        this.providerService = ProviderFactory.getInstance();
    }

    public Object handle(RpcRequest rpcRequest) {
        Object service = providerService.getServiceOrNull(rpcRequest.getRpcServiceName());
        if (service == null) {
            LOGGER.error("The instance of [{}] could not be found", rpcRequest.getRpcServiceName());
            throw new RuntimeException("Service instance could not be found");
        }
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
