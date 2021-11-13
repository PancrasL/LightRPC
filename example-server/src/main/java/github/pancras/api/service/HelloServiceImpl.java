package github.pancras.api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import github.pancras.Hello;
import github.pancras.HelloService;

/**
 * @author pancras
 */

public class HelloServiceImpl implements HelloService {
    private static final Logger LOGGER = LoggerFactory.getLogger(HelloServiceImpl.class);

    @Override
    public String hello(Hello hello) {
        String result = "[API impl] Hello, " + hello.getMessage();
        return result;
    }

    @Override
    public String hello(String hello) {
        String result = "[API impl] Hello World!" + hello;
        return result;
    }
}
