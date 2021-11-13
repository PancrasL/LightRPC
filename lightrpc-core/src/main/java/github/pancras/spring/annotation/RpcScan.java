package github.pancras.spring.annotation;

import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import github.pancras.spring.RpcScannerRegistrar;

/**
 * @author PancrasL
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Import(RpcScannerRegistrar.class)
@Documented
public @interface RpcScan {

    String[] basePackage();

}
