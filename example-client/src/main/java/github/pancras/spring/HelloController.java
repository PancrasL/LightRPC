package github.pancras.spring;

import org.springframework.stereotype.Component;

import github.pancras.HelloService;
import github.pancras.spring.annotation.RpcReference;

@Component
public class HelloController {
    /**
     * 需要和example-server模块下的indi.pancras.spring.service.HelloServiceImpl2一致
     */
    @RpcReference
    private HelloService helloService;

    public void test() {
        for (int i = 0; i < 5; i++) {
            String s = helloService.hello("Mike.");
            System.out.println(s);
        }
    }
}
