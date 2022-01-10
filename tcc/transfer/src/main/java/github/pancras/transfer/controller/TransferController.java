package github.pancras.transfer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import github.pancras.transfer.service.TransferService;

@RestController
public class TransferController {
    @Autowired
    TransferService service;

    @RequestMapping("/test")
    public String handle01() {
        service.doTransfer(null);
        return "Hello, Spring Boot 2!";
    }
}
