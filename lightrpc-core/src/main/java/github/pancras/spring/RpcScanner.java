package github.pancras.spring;

import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.lang.annotation.Annotation;
import java.util.Set;

import javax.annotation.Nonnull;

/**
 * 扫描包路径basePackages，返回路径下拥有annoType注解的bean
 * @author PancrasL
 */
public class RpcScanner extends ClassPathBeanDefinitionScanner {

    public RpcScanner(BeanDefinitionRegistry registry, Class<? extends Annotation> annoType) {
        super(registry);
        super.addIncludeFilter(new AnnotationTypeFilter(annoType));
    }

    @Nonnull
    @Override
    protected Set<BeanDefinitionHolder> doScan(@Nonnull String... basePackages) {
        return super.doScan(basePackages);
    }
}