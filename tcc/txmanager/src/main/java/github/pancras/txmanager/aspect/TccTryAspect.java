package github.pancras.txmanager.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.util.UUID;

import github.pancras.txmanager.ResourceManager;
import github.pancras.txmanager.annotation.TccTry;
import github.pancras.txmanager.dto.BranchTx;
import github.pancras.txmanager.dto.TccActionContext;
import lombok.extern.slf4j.Slf4j;

/**
 * 在执行分支事务前将分支事务的状态上报到
 */
@Aspect
@Slf4j
public class TccTryAspect {
    private final ResourceManager RM = ResourceManager.INSTANCE;

    @Around("@annotation(github.pancras.txmanager.annotation.TccTry)")
    public Object invoke(ProceedingJoinPoint point) {
        // 1. 获取方法的注解信息
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        TccTry tccTry = method.getAnnotation(TccTry.class);

        // 2. 注册分支事务
        TccActionContext context = (TccActionContext) point.getArgs()[0];
        RM.registResource(point.getTarget());
        registBranch(context, tccTry, point.getTarget().getClass().getCanonicalName());
        try {
            Object result = point.proceed();
            // 2.1 tccTry执行成功
            System.out.println("TccTry success:" + point.getTarget().getClass().getCanonicalName());
            return result;
        } catch (Throwable throwable) {
            System.out.println("TccTry fail:" + point.getTarget().getClass().getCanonicalName());
            throwable.printStackTrace();
            // 2.2 tccTry执行失败，返回false
            return false;
        }
    }

    private void registBranch(TccActionContext context, TccTry tccTry, String resourceId) {
        String branchId = UUID.randomUUID().toString();
        BranchTx branch = new BranchTx(context.getXid(), branchId, tccTry.commitMethod(), tccTry.rollbackMethod(), resourceId, RM.getAddress());
        RM.writeBranchTx(branch);
    }
}
