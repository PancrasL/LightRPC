package github.pancras.tcc;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import github.pancras.tcc.annotation.LocalTcc;

public class TccScanner implements BeanPostProcessor {
    private ZkTccHandler zkTccHandler;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean.getClass().isAnnotationPresent(LocalTcc.class)) {
            //String className = point.getSignature().getDeclaringTypeName();
            //String uuid = point.getArgs()[0].toString();
            String uuid = "1";
            String className = "a";
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
                @Override
                public void afterCompletion(int status) {
                    zkTccHandler.registMethod(uuid, className);
                }
            });
        }
        return bean;
    }
}
