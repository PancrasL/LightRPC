package github.pancras.spring.service;

import github.pancras.Hello;
import github.pancras.HelloService;
import github.pancras.spring.annotation.RpcService;

@RpcService
public class HelloServiceImpl2 implements HelloService {
    @Override
    public String hello(Hello hello) {
        String result = "[Spring Annotation Impl] Hello, " + hello.getMessage();
        return result;
    }

    @Override
    public String hello(String hello) {
        String result = "[Spring Annotation Impl] Hello World!" + hello;
        return result;
    }
}
