package github.pancras.txmanager.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.util.List;
import java.util.UUID;

import github.pancras.txmanager.BranchTransaction;
import github.pancras.txmanager.RootContext;
import lombok.extern.slf4j.Slf4j;

@Aspect
@Slf4j
public class TccGlobalAspect {

    @Around("@annotation(github.pancras.txmanager.annotation.TccGlobal)")
    public Object invoke(ProceedingJoinPoint point) {
        log.warn("切入TccGloabl");
        // 1. 创建全局事务ID
        RootContext.setContext("xid", UUID.randomUUID().toString());
        Object result = null;

        // 2. 执行全局事务TccGloabl
        try {
//            Object[] args = point.getArgs();
//            args[0] = new TccActionContext(RootContext.getXid(), null);
            result = point.proceed();
            // 2.1 @TccGlobal方法执行成功，获取参与的所有分支事务，执行commit()方法
            List<BranchTransaction> branches = RootContext.getBranchTransaction(RootContext.getXid());
            System.out.println("需要commit的分支数：" + branches.size());
            for (BranchTransaction branch : branches) {
                doCommit(branch);
            }
        } catch (Throwable e) {
            // 2.2 @TccGlobal方法执行失败，获取参与的所有分支事务，执行rollback()方法
            List<BranchTransaction> branches = RootContext.getBranchTransaction(RootContext.getXid());
            System.out.println("需要rollback的分支数：" + branches.size());
            for (BranchTransaction branch : branches) {
                doCancel(branch);
            }
            e.printStackTrace();
        }
        return result;
    }

    private void doCommit(BranchTransaction branch) {
        System.out.println("提交事务：" + branch);
    }

    private void doCancel(BranchTransaction branch) {
        System.out.println("回滚事务" + branch);
    }
}
