package github.pancras.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

import javax.annotation.Nonnull;

import github.pancras.provider.ProviderService;
import github.pancras.provider.impl.DefaultProviderServiceImpl;
import github.pancras.registry.RegistryFactory;
import github.pancras.remoting.transport.RpcClient;
import github.pancras.remoting.transport.netty.client.NettyRpcClient;
import github.pancras.spring.annotation.RpcReference;
import github.pancras.spring.annotation.RpcService;
import github.pancras.wrapper.RpcReferenceConfig;


/**
 * @author PancrasL
 */
@Component
public class SpringBeanPostProcessor implements BeanPostProcessor {
    private final Logger LOGGER = LoggerFactory.getLogger(SpringBeanPostProcessor.class);

    private final ProviderService provider;
    private final RpcClient rpcClient;

    public SpringBeanPostProcessor() {
        provider = DefaultProviderServiceImpl.newInstance(RegistryFactory.getInstance());
        rpcClient = NettyRpcClient.getInstance();
    }

    /**
     * 发布拥有@RpcServer的Bean
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, @Nonnull String beanName) throws BeansException {
        if (bean.getClass().isAnnotationPresent(RpcService.class)) {
            LOGGER.info("[{}] is annotated with  [{}]", bean.getClass().getName(), RpcService.class.getCanonicalName());
            RpcService rpcService = bean.getClass().getAnnotation(RpcService.class);
            //new RpcServiceConfig.Builder<Object>()
            //RpcServiceConfig serviceConfig = RpcServiceConfig.newInstance(bean, rpcService.group(), rpcService.version());
            try {
                //provider.publishService(serviceConfig);
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
            }
        }
        return bean;
    }

    /**
     * 代理Bean中拥有@RpcReference的域
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, @Nonnull String beanName) throws BeansException {
        Class<?> targetClass = bean.getClass();
        Field[] declaredFields = targetClass.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            RpcReference rpcReference = declaredField.getAnnotation(RpcReference.class);
            if (rpcReference != null) {
                Class interfac = declaredField.getClass();
                RpcReferenceConfig<Object> reference = new RpcReferenceConfig.Builder<Object>(rpcClient, interfac).build();
                Object referent = reference.getReferent();
                declaredField.setAccessible(true);
                try {
                    declaredField.set(bean, referent);
                } catch (IllegalAccessException e) {
                    LOGGER.error(e.getMessage());
                }
            }
        }
        return bean;
    }
}
