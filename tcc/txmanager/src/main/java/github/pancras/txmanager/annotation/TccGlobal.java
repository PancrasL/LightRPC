package github.pancras.txmanager.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// TCC全局事务，被该注解标记的方法会调用若干个分支事务，即被TccTry标记的方法
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TccGlobal {
    String name() default "";
}
