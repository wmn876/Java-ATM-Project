package com.uni.atm.service;


import com.uni.atm.domain.*;
import com.uni.atm.exception.AuthFailedException;
import com.uni.atm.exception.InvalidRequestException;
import com.uni.atm.exception.ResourceNotExistException;
import com.uni.atm.repo.AccountRepo;
import com.uni.atm.repo.CardRepo;
import com.uni.atm.requests.TransactionRequest;
import com.uni.atm.requests.TransactionType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

public class CardServiceTest {

    private CardRepo cardRepo = Mockito.mock(CardRepo.class);
    private AccountRepo accountRepo = Mockito.mock(AccountRepo.class);
    private CardService cardService = new CardService(cardRepo, accountRepo);

    @Test
    public void withDrawMoneySuccessfully() {
        TransactionRequest transactionRequest = new TransactionRequest();
        String cardNo = "3496746711713539";
        String pin = "6604";
        transactionRequest.setCardNo(cardNo);
        transactionRequest.setTransactionType(TransactionType.WITHDRAW);
        transactionRequest.setAmount(BigDecimal.valueOf(100.0));
        transactionRequest.setPin(pin);

        Account account = new Account().accountNo("14360420399329242").accountType(AccountType.SAVING).balance(BigDecimal.valueOf(2000.0)).id(123456L);
        Customer customer = new Customer().firstName("Bob").lastName("Smith").id(123456L);
        Card card = new Card().cardType(CardType.DEBT).cardNo(cardNo).id(123456L).pin(pin).expireDate(LocalDate.now().plusYears(3)).issueDate(LocalDate.now()).account(account).customer(customer);
        account.customer(customer);
        account.card(card);
        customer.cards(List.of(card)).accounts(List.of(account));
        card.customer(customer);
        card.account(account);
        when(cardRepo.findByCardNo(cardNo)).thenReturn(Optional.of(card));
        BigDecimal balance = cardService.performTransaction(transactionRequest);
        Assertions.assertEquals(BigDecimal.valueOf(1900.0), balance);
    }

    @Test
    public void shouldFailWhenTryingWithDrawMoneyMoreThanBalance() {
        TransactionRequest transactionRequest = new TransactionRequest();
        String cardNo = "3496746711713539";
        String pin = "6604";
        transactionRequest.setCardNo(cardNo);
        transactionRequest.setTransactionType(TransactionType.WITHDRAW);
        transactionRequest.setAmount(BigDecimal.valueOf(2100.0));
        transactionRequest.setPin(pin);

        Account account = new Account().accountNo("14360420399329242").accountType(AccountType.SAVING).balance(BigDecimal.valueOf(2000.0)).id(123456L);
        Customer customer = new Customer().firstName("Bob").lastName("Smith").id(123456L);
        Card card = new Card().cardType(CardType.DEBT).cardNo(cardNo).id(123456L).pin(pin).expireDate(LocalDate.now().plusYears(3)).issueDate(LocalDate.now()).account(account).customer(customer);
        account.customer(customer);
        account.card(card);
        customer.cards(List.of(card)).accounts(List.of(account));
        card.customer(customer);
        card.account(account);
        when(cardRepo.findByCardNo(cardNo)).thenReturn(Optional.of(card));

        InvalidRequestException invalidRequestException = Assertions.assertThrowsExactly(InvalidRequestException.class, () -> cardService.performTransaction(transactionRequest));
        Assertions.assertEquals("Insufficient Balance", invalidRequestException.getMessage());
    }

    @Test
    public void shouldFailWhenTryingWithDrawZeroMoney() {
        TransactionRequest transactionRequest = new TransactionRequest();
        String cardNo = "3496746711713539";
        String pin = "6604";
        transactionRequest.setCardNo(cardNo);
        transactionRequest.setTransactionType(TransactionType.WITHDRAW);
        transactionRequest.setAmount(BigDecimal.valueOf(0.0));
        transactionRequest.setPin(pin);

        Account account = new Account().accountNo("14360420399329242").accountType(AccountType.SAVING).balance(BigDecimal.valueOf(2000.0)).id(123456L);
        Customer customer = new Customer().firstName("Bob").lastName("Smith").id(123456L);
        Card card = new Card().cardType(CardType.DEBT).cardNo(cardNo).id(123456L).pin(pin).expireDate(LocalDate.now().plusYears(3)).issueDate(LocalDate.now()).account(account).customer(customer);
        account.customer(customer);
        account.card(card);
        customer.cards(List.of(card)).accounts(List.of(account));
        card.customer(customer);
        card.account(account);
        when(cardRepo.findByCardNo(cardNo)).thenReturn(Optional.of(card));

        InvalidRequestException invalidRequestException = Assertions.assertThrowsExactly(InvalidRequestException.class, () -> cardService.performTransaction(transactionRequest));
        Assertions.assertEquals("For Withdraw: Amount Can't be empty", invalidRequestException.getMessage());
    }

    @Test
    public void shouldFailWhenTryingDepositZeroMoney() {
        TransactionRequest transactionRequest = new TransactionRequest();
        String cardNo = "3496746711713539";
        String pin = "6604";
        transactionRequest.setCardNo(cardNo);
        transactionRequest.setTransactionType(TransactionType.DEPOSIT);
        transactionRequest.setAmount(BigDecimal.valueOf(0.0));
        transactionRequest.setPin(pin);

        Account account = new Account().accountNo("14360420399329242").accountType(AccountType.SAVING).balance(BigDecimal.valueOf(2000.0)).id(123456L);
        Customer customer = new Customer().firstName("Bob").lastName("Smith").id(123456L);
        Card card = new Card().cardType(CardType.DEBT).cardNo(cardNo).id(123456L).pin(pin).expireDate(LocalDate.now().plusYears(3)).issueDate(LocalDate.now()).account(account).customer(customer);
        account.customer(customer);
        account.card(card);
        customer.cards(List.of(card)).accounts(List.of(account));
        card.customer(customer);
        card.account(account);
        when(cardRepo.findByCardNo(cardNo)).thenReturn(Optional.of(card));

        InvalidRequestException invalidRequestException = Assertions.assertThrowsExactly(InvalidRequestException.class, () -> cardService.performTransaction(transactionRequest));
        Assertions.assertEquals("For Deposit: Amount Can't be empty", invalidRequestException.getMessage());
    }

    @Test
    public void depositMoneySuccessfully() {
        TransactionRequest transactionRequest = new TransactionRequest();
        String cardNo = "3496746711713539";
        String pin = "6604";
        transactionRequest.setCardNo(cardNo);
        transactionRequest.setTransactionType(TransactionType.DEPOSIT);
        transactionRequest.setAmount(BigDecimal.valueOf(100.0));
        transactionRequest.setPin(pin);

        Account account = new Account().accountNo("14360420399329242").accountType(AccountType.SAVING).balance(BigDecimal.valueOf(2000.0)).id(123456L);
        Customer customer = new Customer().firstName("Bob").lastName("Smith").id(123456L);
        Card card = new Card().cardType(CardType.DEBT).cardNo(cardNo).id(123456L).pin(pin).expireDate(LocalDate.now().plusYears(3)).issueDate(LocalDate.now()).account(account).customer(customer);
        account.customer(customer);
        account.card(card);
        customer.cards(List.of(card)).accounts(List.of(account));
        card.customer(customer);
        card.account(account);
        when(cardRepo.findByCardNo(cardNo)).thenReturn(Optional.of(card));
        BigDecimal balance = cardService.performTransaction(transactionRequest);
        Assertions.assertEquals(BigDecimal.valueOf(2100.0), balance);
    }

    @Test
    public void balanceInquirySuccessfully() {
        TransactionRequest transactionRequest = new TransactionRequest();
        String cardNo = "3496746711713539";
        String pin = "6604";
        transactionRequest.setCardNo(cardNo);
        transactionRequest.setTransactionType(TransactionType.BALANCE_INQUIRY);
        transactionRequest.setPin(pin);

        Account account = new Account().accountNo("14360420399329242").accountType(AccountType.SAVING).balance(BigDecimal.valueOf(2000.0)).id(123456L);
        Customer customer = new Customer().firstName("Bob").lastName("Smith").id(123456L);
        Card card = new Card().cardType(CardType.DEBT).cardNo(cardNo).id(123456L).pin(pin).expireDate(LocalDate.now().plusYears(3)).issueDate(LocalDate.now()).account(account).customer(customer);
        account.customer(customer);
        account.card(card);
        customer.cards(List.of(card)).accounts(List.of(account));
        card.customer(customer);
        card.account(account);
        when(cardRepo.findByCardNo(cardNo)).thenReturn(Optional.of(card));
        BigDecimal balance = cardService.performTransaction(transactionRequest);
        Assertions.assertEquals(BigDecimal.valueOf(2000.0), balance);
    }

    @Test
    public void validateCardSuccessfully() {

        String cardNo = "3496746711713539";
        String pin = "6604";

        Account account = new Account().accountNo("14360420399329242").accountType(AccountType.SAVING).balance(BigDecimal.valueOf(2000.0)).id(123456L);
        Customer customer = new Customer().firstName("Bob").lastName("Smith").id(123456L);
        Card card = new Card().cardType(CardType.DEBT).cardNo(cardNo).id(123456L).pin(pin).expireDate(LocalDate.now().plusYears(3)).issueDate(LocalDate.now()).account(account).customer(customer);
        account.customer(customer);
        account.card(card);
        customer.cards(List.of(card)).accounts(List.of(account));
        card.customer(customer);
        card.account(account);
        when(cardRepo.findByCardNo(cardNo)).thenReturn(Optional.of(card));
        Card validatedCard = cardService.validateCard(cardNo, pin);
        Assertions.assertEquals(cardNo, validatedCard.cardNo());
        Assertions.assertEquals("Bob", validatedCard.customer().firstName());
        Assertions.assertEquals("Smith", validatedCard.customer().lastName());

    }

    @Test
    public void shouldFailValidationForWrongPin() {

        String cardNo = "3496746711713539";
        String pin = "6604";

        Account account = new Account().accountNo("14360420399329242").accountType(AccountType.SAVING).balance(BigDecimal.valueOf(2000.0)).id(123456L);
        Customer customer = new Customer().firstName("Bob").lastName("Smith").id(123456L);
        Card card = new Card().cardType(CardType.DEBT).cardNo(cardNo).id(123456L).pin(pin).expireDate(LocalDate.now().plusYears(3)).issueDate(LocalDate.now()).account(account).customer(customer);
        account.customer(customer);
        account.card(card);
        customer.cards(List.of(card)).accounts(List.of(account));
        card.customer(customer);
        card.account(account);
        when(cardRepo.findByCardNo(cardNo)).thenReturn(Optional.of(card));
        AuthFailedException authFailedException = Assertions.assertThrows(AuthFailedException.class, () -> cardService.validateCard(cardNo, "0000"));
        Assertions.assertEquals("Invalid Pin", authFailedException.getMessage());
    }

    @Test
    public void shouldFailValidationForInvalidCard() {

        String cardNo = "3496746711713539";
        String pin = "6604";

        Account account = new Account().accountNo("14360420399329242").accountType(AccountType.SAVING).balance(BigDecimal.valueOf(2000.0)).id(123456L);
        Customer customer = new Customer().firstName("Bob").lastName("Smith").id(123456L);
        Card card = new Card().cardType(CardType.DEBT).cardNo(cardNo).id(123456L).pin(pin).expireDate(LocalDate.now().plusYears(3)).issueDate(LocalDate.now()).account(account).customer(customer);
        account.customer(customer);
        account.card(card);
        customer.cards(List.of(card)).accounts(List.of(account));
        card.customer(customer);
        card.account(account);
        when(cardRepo.findByCardNo(cardNo)).thenReturn(Optional.of(card));
        ResourceNotExistException authFailedException = Assertions.assertThrows(ResourceNotExistException.class, () -> cardService.validateCard("1234567890123456", "0000"));
        Assertions.assertEquals("Card Not Found", authFailedException.getMessage());
    }
}
