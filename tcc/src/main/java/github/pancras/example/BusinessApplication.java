package github.pancras.example;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import github.pancras.spring.annotation.RpcScan;

@RpcScan(basePackage = "github.pancras.*")
public class BusinessApplication {
    public static void main(String[] args) throws Exception {
        ApplicationContext context = new AnnotationConfigApplicationContext(BusinessApplication.class);
        for (String beanDefinitionName : context.getBeanDefinitionNames()) {
            System.out.println(beanDefinitionName);
        }
        MyController bean = context.getBean(MyController.class);
        System.in.read();
    }
}
