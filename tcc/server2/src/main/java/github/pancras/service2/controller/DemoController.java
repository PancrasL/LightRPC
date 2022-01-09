package github.pancras.service2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import github.pancras.service2.service.DemoService;

/**
 * @author pancras
 * @create 2021/4/27 16:08
 */

//@ResponseBody
//@Controller
@RestController
public class DemoController {

    @Autowired
    DemoService service2;

    @RequestMapping("/test")
    public String handle01() {
        service2.test();
        return "Hello, Spring Boot 2!";
    }
}
