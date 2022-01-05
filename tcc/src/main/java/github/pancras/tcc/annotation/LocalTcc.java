package github.pancras.tcc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface LocalTcc {
    // 对应的提交方法
    String confirm() default "confirm";

    // 对应的回滚方法
    String cancel() default "cancel";

    // 是否为主方法
    boolean isMain() default false;
}
