package com.uni.atm.service;

import java.time.LocalDate;
import java.util.List;

import javax.transaction.Transactional;

import com.uni.atm.domain.*;
import com.uni.atm.repo.AccountRepo;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.uni.atm.repo.CardRepo;
import com.uni.atm.repo.CustomerRepo;
import com.uni.atm.requests.CustomerRequest;
import com.uni.atm.response.CustomerResponse;
import com.uni.atm.util.Utils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CustomerService {

    private final AccountRepo accountRepo;
    private final CustomerRepo customerRepo;
    private final CardRepo cardRepo;
    private final Integer cardExpireYears;

    public CustomerService(AccountRepo accountRepo, CustomerRepo customerRepo, CardRepo cardRepo, @Value("${card.expire.year}") Integer cardExpireYears) {
        this.accountRepo = accountRepo;
        this.customerRepo = customerRepo;
        this.cardRepo = cardRepo;
        this.cardExpireYears = cardExpireYears;
    }

    @Transactional
    public CustomerResponse onBoardCustomer(CustomerRequest customerRequest) {

        Customer customer = new Customer().firstName(customerRequest.getFirstName()).lastName(customerRequest.getLastName());
        customerRepo.save(customer);

        Account savingAccount = new Account().accountNo(Utils.generateAccountNumber()).accountType(AccountType.SAVING).customer(customer).balance(customerRequest.getInitialBalance());
        accountRepo.save(savingAccount);


        Pair<String, String> cardNumberAndPin = Utils.generateCardNumberWithPin();
        Card debitCard = new Card().cardType(CardType.DEBT).cardNo(cardNumberAndPin.getLeft()).issueDate(LocalDate.now()).expireDate(LocalDate.now().plusYears(cardExpireYears)).pin(cardNumberAndPin.getRight()).customer(customer);
        cardRepo.save(debitCard);

        debitCard.account(savingAccount);
        savingAccount.card(debitCard);

        customer.cards(List.of(debitCard));
        customer.accounts(List.of(savingAccount));

        log.info(String.format("For Sample Tests: cardNo: %s , pin: %s, accountNo: %s", debitCard.cardNo(), debitCard.pin(), savingAccount.accountNo()));

        return new CustomerResponse(customer.firstName(), customer.lastName(), debitCard.cardNo(), savingAccount.accountNo());
    }

}