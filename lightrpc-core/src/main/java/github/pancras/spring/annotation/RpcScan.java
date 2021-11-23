package github.pancras.spring.annotation;

import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import github.pancras.spring.RpcScannerRegistrar;

/**
 * 标识需要扫描的包路径
 *
 * @author PancrasL
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Import(RpcScannerRegistrar.class)
@Documented
public @interface RpcScan {

    String[] basePackage();

}
