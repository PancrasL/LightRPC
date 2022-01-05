package github.pancras.tcc;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import github.pancras.tcc.annotation.LocalTcc;

@Aspect
@Component
public class TccTryAspect {

    private ZkTccHandler zkTccHandler;

    @Pointcut("execution(* github.pancras.tcc.service.Impl.*(..))")
    public void pointcutName() {
    }

    @Before("pointcutName()")
    public void permissionCheck(JoinPoint point) {
        String className = point.getSignature().getDeclaringTypeName();
        String uuid = point.getArgs()[0].toString();
        try {
            LocalTcc localTcc = Class.forName(className).getAnnotation(LocalTcc.class);
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
                @Override
                public void afterCompletion(int status) {
                    switch (status) {
                        case 0:
                            zkTccHandler.registMethod(uuid, className);
                            break;
                        case 1:
                            // transaction status is rollback
                            zkTccHandler.registMethod(uuid, className);
                            break;
                        default:
                            throw new IllegalStateException("Unknown tcc transaction status: " + status);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
