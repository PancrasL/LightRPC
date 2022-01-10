package github.pancras.transfer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import github.pancras.spring.annotation.RpcScan;
import github.pancras.txmanager.annotation.EnableTcc;

@SpringBootApplication
@EnableTcc
@RpcScan(basePackage = {""})
public class TransferApplication {
    public static void main(String[] args) {
        SpringApplication.run(TransferApplication.class, args);
    }
}
