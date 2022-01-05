package github.pancras.example;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import github.pancras.example.service.BalanceService;
import github.pancras.example.service.OrderService;
import github.pancras.example.service.StockService;
import github.pancras.spring.annotation.RpcReference;
import github.pancras.spring.annotation.RpcScan;

@RpcScan(basePackage = "github.pancras.*")
public class BusinessApplication {
    @RpcReference
    private BalanceService balance;
    @RpcReference
    private OrderService order;
    @RpcReference
    private StockService stock;

    public static void main(String[] args) throws Exception {
        ApplicationContext context = new AnnotationConfigApplicationContext(BusinessApplication.class);
        BusinessApplication bean = context.getBean(BusinessApplication.class);
        bean.balance.balanceTry();
        bean.order.orderTry();
        bean.stock.stockTry();
    }
}
