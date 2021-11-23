package github.pancras.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import javax.annotation.Nonnull;

import github.pancras.remoting.transport.RpcServer;
import github.pancras.spring.annotation.RpcService;
import github.pancras.wrapper.RpcServiceConfig;

public class RpcServicePostProcessor implements BeanPostProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(RpcServicePostProcessor.class);
    private final RpcServer rpcServer;

    public RpcServicePostProcessor(RpcServer rpcServer) {
        this.rpcServer = rpcServer;
    }

    /**
     * 发布@RpcServer
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, @Nonnull String beanName) throws BeansException {
        if (bean.getClass().isAnnotationPresent(RpcService.class)) {
            LOGGER.info("[{}] is annotated with  [{}]", bean.getClass().getName(), RpcService.class.getCanonicalName());
            RpcService rpcService = bean.getClass().getAnnotation(RpcService.class);
            RpcServiceConfig<Object> serviceConfig = new RpcServiceConfig.Builder<>(bean)
                    .group(rpcService.group())
                    .version(rpcService.version())
                    .build();
            try {
                rpcServer.registerService(serviceConfig);
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
            }
        }
        return bean;
    }
}
