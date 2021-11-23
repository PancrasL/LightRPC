package github.pancras.api.multithread.service;

import github.pancras.Hello;
import github.pancras.HelloService;

/**
 * 阻塞1秒，用来测试多线程版本
 */
public class BlockHelloServiceImpl implements HelloService {
    @Override
    public String hello(Hello hello) {
        String result = "[API impl] Hello, " + hello.getMessage();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public String hello(String hello) {
        String result = "[API impl] Hello World!" + hello;
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }
}
