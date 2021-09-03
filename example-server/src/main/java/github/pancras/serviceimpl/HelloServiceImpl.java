package github.pancras.serviceimpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import github.pancras.Hello;
import github.pancras.HelloService;

/**
 * @author pancras
 * @create 2021/6/3 19:00
 */

public class HelloServiceImpl implements HelloService {
    private static final Logger LOGGER = LoggerFactory.getLogger(HelloServiceImpl.class);

    @Override
    public String hello(Hello hello) {
        LOGGER.info("HelloServiceImpl receive：{}", hello.toString());
        String result = "Hello, " + hello.getMessage();
        LOGGER.info("HelloServiceImpl return:{}", result);
        return result;
    }

    @Override
    public String hello(String hello) {
        LOGGER.info("HelloServiceImpl receive：{}", hello);
        String result = "Hello World!" + hello;
        LOGGER.info("HelloServiceImpl return:{}", result);
        return result;
    }
}
