package github.pancras.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

import github.pancras.commons.factory.SingletonFactory;
import github.pancras.provider.ProviderFactory;
import github.pancras.provider.ProviderService;
import github.pancras.proxy.RpcClientProxy;
import github.pancras.remoting.transport.RpcClient;
import github.pancras.remoting.transport.netty.client.NettyRpcClient;
import github.pancras.spring.annotation.RpcReference;
import github.pancras.spring.annotation.RpcService;
import github.pancras.wrapper.RpcServiceConfig;

/**
 * @author PancrasL
 */
@Component
public class SpringBeanPostProcessor implements BeanPostProcessor {
    private final Logger LOGGER = LoggerFactory.getLogger(SpringBeanPostProcessor.class);

    private final ProviderService provider;
    private final RpcClient rpcClient;

    public SpringBeanPostProcessor() {
        provider = ProviderFactory.getInstance();
        this.rpcClient = SingletonFactory.getInstance(NettyRpcClient.class);
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean.getClass().isAnnotationPresent(RpcService.class)) {
            LOGGER.info("[{}] is annotated with  [{}]", bean.getClass().getName(), RpcService.class.getCanonicalName());
            // get RpcService annotation
            RpcService rpcService = bean.getClass().getAnnotation(RpcService.class);
            // build RpcServiceProperties
            RpcServiceConfig serviceConfig = new RpcServiceConfig(rpcService);
            try {
                provider.publishService(serviceConfig);
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
            }
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> targetClass = bean.getClass();
        Field[] declaredFields = targetClass.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            RpcReference rpcReference = declaredField.getAnnotation(RpcReference.class);
            if (rpcReference != null) {
                RpcClientProxy rpcClientProxy = new RpcClientProxy(rpcClient);
                Object clientProxy = rpcClientProxy.getProxy(declaredField.getType());
                declaredField.setAccessible(true);
                try {
                    declaredField.set(bean, clientProxy);
                } catch (IllegalAccessException e) {
                    LOGGER.error(e.getMessage());
                }
            }
        }
        return bean;
    }
}
