package github.pancras.remoting.handler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import github.pancras.provider.ProviderService;
import github.pancras.remoting.dto.RpcRequest;

/**
 * @author PancrasL
 * <p>
 * 获取到服务对象，并利用反射机制在服务器端执行RPC方法
 */
public class RpcRequestHandler {
    private final ProviderService providerService;

    private RpcRequestHandler(ProviderService providerService) {
        this.providerService = providerService;
    }

    public static RpcRequestHandler newInstance(ProviderService providerService) {
        return new RpcRequestHandler(providerService);
    }

    public Object handle(RpcRequest rpcRequest) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Object service = providerService.getService(rpcRequest.getRpcServiceName());
        return invokeTargetMethod(rpcRequest, service);
    }

    private Object invokeTargetMethod(RpcRequest rpcRequest, Object service) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Object result;
        Method method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
        result = method.invoke(service, rpcRequest.getParameters());

        return result;
    }
}
