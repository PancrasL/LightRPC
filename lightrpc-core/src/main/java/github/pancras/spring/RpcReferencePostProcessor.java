package github.pancras.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Field;

import javax.annotation.Nonnull;

import github.pancras.remoting.transport.RpcClient;
import github.pancras.spring.annotation.RpcReference;
import github.pancras.wrapper.RpcReferenceConfig;

public class RpcReferencePostProcessor implements BeanPostProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(RpcReferencePostProcessor.class);
    private final RpcClient rpcClient;

    public RpcReferencePostProcessor(RpcClient rpcClient) {
        this.rpcClient = rpcClient;
    }

    /**
     * 代理@RpcReference
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, @Nonnull String beanName) throws BeansException {
        Class<?> targetClass = bean.getClass();
        Field[] declaredFields = targetClass.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            RpcReference rpcReference = declaredField.getAnnotation(RpcReference.class);
            if (rpcReference != null) {
                Class interfac = declaredField.getType();
                RpcReferenceConfig<Object> reference = new RpcReferenceConfig.Builder<Object>(rpcClient, interfac)
                        .version(rpcReference.version())
                        .group(rpcReference.group())
                        .build();
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
