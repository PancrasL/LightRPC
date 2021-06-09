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
    private static final Logger logger = LoggerFactory.getLogger(HelloServiceImpl.class);

    public String hello(Hello hello) {
        logger.info("HelloServiceImpl receive：{}", hello.toString());
        String result = "Hello, " + hello.getMessage();
        logger.info("HelloServiceImpl return:{}", result);
        return result;
    }

    public String hello(String hello) {
        logger.info("HelloServiceImpl receive：{}", hello);
        String result = "Hello World!" + hello;
        logger.info("HelloServiceImpl return:{}", result);
        return result;
    }
}
