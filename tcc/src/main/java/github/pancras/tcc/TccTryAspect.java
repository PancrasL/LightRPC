package github.pancras.tcc;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TccTryAspect {
    @Pointcut("execution(* github.pancras.service.impl.*.*(..))")
    public void pointcutName() {
    }
}
