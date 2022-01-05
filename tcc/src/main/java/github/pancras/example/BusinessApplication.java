package github.pancras.example;

import github.pancras.example.service.BalanceService;
import github.pancras.example.service.OrderService;
import github.pancras.example.service.StockService;
import github.pancras.spring.annotation.RpcReference;

public class BusinessApplication {
    @RpcReference
    private BalanceService balance;
    @RpcReference
    private OrderService order;
    @RpcReference
    private StockService stock;


}
