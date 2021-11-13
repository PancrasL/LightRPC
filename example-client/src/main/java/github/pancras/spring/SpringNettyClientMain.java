package github.pancras.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import github.pancras.spring.annotation.RpcScan;

@RpcScan(basePackage = "github.pancras.spring")
public class SpringNettyClientMain {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(SpringNettyClientMain.class);
        HelloController controller = context.getBean(HelloController.class);
        controller.test();
    }
}
