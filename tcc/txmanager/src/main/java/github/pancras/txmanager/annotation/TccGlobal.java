package github.pancras.txmanager.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 被@TccGlobal注解的方法是TCC全局事务(GlobalTx)，在执行该方法时会被TccTryAspect切入
 * <p>
 * 该方法内部会调用多个@TccTry方法，当且仅当所有的TccTry方法执行成功时，才会调用分支事务的commit方法，否则调用rollback方法
 * </p>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TccGlobal {
    String name() default "";
}
