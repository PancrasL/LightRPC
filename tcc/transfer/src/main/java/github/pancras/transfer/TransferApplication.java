package github.pancras.transfer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import github.pancras.txmanager.annotation.EnableTcc;

@SpringBootApplication
@EnableTcc
public class TransferApplication {
    public static void main(String[] args) {
        SpringApplication.run(TransferApplication.class, args);
    }
}
