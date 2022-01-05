package github.pancras.example;

import org.springframework.stereotype.Component;

import github.pancras.example.service.BalanceService;
import github.pancras.example.service.OrderService;
import github.pancras.example.service.StockService;
import github.pancras.spring.annotation.RpcReference;

@Component
public class MyController {
    @RpcReference
    private BalanceService balance;
    @RpcReference
    private OrderService order;
    @RpcReference
    private StockService stock;
}
