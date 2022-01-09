package github.pancras.service1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import github.pancras.service1.service.DemoService;

@RestController
public class DemoController {
    @Autowired
    DemoService service;

    @RequestMapping("/test")
    public String handle01() {
        service.test();
        return "Hello, Spring Boot 2!";
    }
}
