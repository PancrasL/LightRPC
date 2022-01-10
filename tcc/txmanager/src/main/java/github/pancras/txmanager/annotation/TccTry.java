package github.pancras.txmanager.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 被@TccTry注解的方法是TCC分支事务(branchTx)，在执行该方法时会被TccTryAspect切入
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface TccTry {
    String commitMethod() default "commit";

    String rollbackMethod() default "rollback";
}
