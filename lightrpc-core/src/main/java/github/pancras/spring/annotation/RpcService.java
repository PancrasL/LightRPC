package github.pancras.spring.annotation;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 服务实例注解，服务端使用
 *
 * @author PancrasL
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited
public @interface RpcService {
    /**
     * 组，区分不同接口的实现类
     */
    String group() default "";

    /**
     * 版本，区分不同版本
     */
    String version() default "";
}
