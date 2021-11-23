package github.pancras.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.StandardAnnotationMetadata;

import javax.annotation.Nonnull;

import github.pancras.spring.annotation.RpcScan;
import github.pancras.spring.annotation.RpcService;


/**
 * 扫描指定包路径下的@RpcService注解项
 *
 * @author PancrasL
 */
public class RpcScannerRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware {
    private static final Logger LOGGER = LoggerFactory.getLogger(RpcScannerRegistrar.class);
    private static final String BASE_PACKAGE_ATTRIBUTE_NAME = "basePackage";
    private ResourceLoader resourceLoader;

    @Override
    public void setResourceLoader(@Nonnull ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, @Nonnull BeanDefinitionRegistry registry) {
        // 获取@RpcScan注解basePackage属性值，如果没有@RpcScan注解则使用Spring默认值
        AnnotationAttributes annoAttributes = AnnotationAttributes.fromMap(metadata.getAnnotationAttributes(RpcScan.class.getName()));
        String[] basePkgs = null;
        if (annoAttributes != null) {
            // 获取“basePackage”的值
            basePkgs = annoAttributes.getStringArray(BASE_PACKAGE_ATTRIBUTE_NAME);
        }
        if (basePkgs == null) {
            basePkgs = new String[]{((StandardAnnotationMetadata) metadata).getIntrospectedClass().getPackage().getName()};
        }

        // 扫描@RpcService注解
        RpcScanner scanner = new RpcScanner(registry, RpcService.class);
        scanner.setResourceLoader(resourceLoader);
        int rpcServiceCount = scanner.scan(basePkgs);
        LOGGER.info("rpcServiceScanner扫描的数量 [{}]", rpcServiceCount);
    }
}
