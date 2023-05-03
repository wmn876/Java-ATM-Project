package com.uni.atm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.uni.atm.requests.CustomerRequest;

import java.math.BigDecimal;

@SpringBootApplication
public class AtmApplication implements CommandLineRunner {

    @Autowired
    private com.uni.atm.service.CustomerService customerService;

    public static void main(String[] args) {
        SpringApplication.run(AtmApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        // Initial Setup for testing
        customerService.onBoardCustomer(new CustomerRequest("Bob", "Smith", BigDecimal.valueOf(1000.00)));
        customerService.onBoardCustomer(new CustomerRequest("Adam", "Bullet", BigDecimal.valueOf(500.00)));
    }

}
