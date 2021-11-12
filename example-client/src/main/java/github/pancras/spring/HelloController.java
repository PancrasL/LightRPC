package github.pancras.spring;

import org.springframework.stereotype.Component;

import github.pancras.HelloService;
import github.pancras.spring.annotation.RpcReference;

@Component
public class HelloController {
    @RpcReference
    private HelloService helloService;


}
