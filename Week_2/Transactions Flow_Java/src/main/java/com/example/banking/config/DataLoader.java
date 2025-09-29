
package com.example.banking.config;

import com.example.banking.model.Account;
import com.example.banking.repository.AccountRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner seed(AccountRepository repo) {
        return args -> {
            if (repo.count() == 0) {
                repo.save(new Account("Alice", new BigDecimal("500.00")));
                repo.save(new Account("Bob", new BigDecimal("250.00")));
            }
        };
    }
}
