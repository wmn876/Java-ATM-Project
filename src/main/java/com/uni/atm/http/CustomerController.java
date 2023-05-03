package com.uni.atm.http;

import javax.validation.Valid;

import com.uni.atm.domain.Card;
import com.uni.atm.domain.Customer;
import com.uni.atm.requests.TransactionRequest;
import com.uni.atm.response.TransactionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.uni.atm.requests.CardValidationRequest;
import com.uni.atm.requests.CustomerRequest;
import com.uni.atm.response.CustomerResponse;
import com.uni.atm.service.CardService;
import com.uni.atm.service.CustomerService;

import java.math.BigDecimal;

@RestController
public class CustomerController {

    private final CustomerService customerService;
    private final CardService cardService;

    public CustomerController(CustomerService customerService, CardService cardService) {
        this.customerService = customerService;
        this.cardService = cardService;
    }

    @PostMapping("/customer")
    public ResponseEntity<CustomerResponse> onBoardCustomer(@RequestBody @Valid CustomerRequest customerRequest) {

        return ResponseEntity.status(HttpStatus.CREATED).body(customerService.onBoardCustomer(customerRequest));
    }

    @PostMapping("/customer-validate")
    public ResponseEntity<CustomerResponse> validateCustomer(
            @RequestBody @Valid CardValidationRequest cardValidationRequest) {
        Card card = cardService.validateCard(cardValidationRequest.getCardNo(), cardValidationRequest.getPin());
        Customer customer = card.customer();
        return ResponseEntity.ok(new CustomerResponse(customer.firstName(), customer.lastName(), card.cardNo(), card.account().accountNo()));
    }

    @PostMapping("/customer-transaction")
    public ResponseEntity<TransactionResponse> transaction(
            @RequestBody @Valid TransactionRequest transactionRequest) {
        BigDecimal balanceAfterTransaction = cardService.performTransaction(transactionRequest);
        return ResponseEntity.ok(new TransactionResponse(true, balanceAfterTransaction));
    }

}
