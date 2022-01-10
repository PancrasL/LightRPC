package github.pancras.txmanager.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.util.UUID;

import github.pancras.txmanager.BranchTransaction;
import github.pancras.txmanager.RootContext;
import github.pancras.txmanager.annotation.TccTry;
import lombok.extern.slf4j.Slf4j;

@Aspect
@Slf4j
public class TccTryAspect {
    @Around("@annotation(github.pancras.txmanager.annotation.TccTry)")
    public Object invoke(ProceedingJoinPoint point) {
        log.info("切入TccTry");
        // 1. 获取方法的注解信息
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        TccTry tccTry = method.getAnnotation(TccTry.class);

        // 2. 注册分支事务
        registBranch(tccTry);
        Object result = null;
        try {
            result = point.proceed();
            // 2.1 tccTry执行成功
            System.out.println("TccTry success:" + point.getTarget().getClass().getCanonicalName());
        } catch (Throwable throwable) {
            System.out.println("TccTry fail:" + point.getTarget().getClass().getCanonicalName());
            throwable.printStackTrace();
        }
        return result;
    }

    private void registBranch(TccTry tccTry) {
        String branchId = UUID.randomUUID().toString();
        BranchTransaction branch = new BranchTransaction(RootContext.getXid(), branchId, tccTry.commitMethod(), tccTry.rollbackMethod());
        RootContext.setBranchTransaction(branch);
        System.out.println("注册分支事务：" + branch);
    }
}
