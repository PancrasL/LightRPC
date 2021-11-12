package github.pancras.spring;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import github.pancras.spring.annotation.RpcScan;

@RpcScan(basePackage = "github.pancras")
public class SpringNettyClientMain {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(SpringNettyClientMain.class);

    }
}
