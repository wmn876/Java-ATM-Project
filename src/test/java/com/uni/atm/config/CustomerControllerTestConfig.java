package com.uni.atm.config;

import com.uni.atm.repo.AccountRepo;
import com.uni.atm.repo.CardRepo;
import com.uni.atm.service.CardService;
import com.uni.atm.service.CustomerService;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class CustomerControllerTestConfig {

    @MockBean
    private CardRepo cardRepo;

    @MockBean
    private CustomerService customerService;

    @Bean
    public CardService customerService() {
        return new CardService(cardRepo, Mockito.mock(AccountRepo.class));
    }
}
