package com.uni.atm.http;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.uni.atm.config.CustomerControllerTestConfig;
import com.uni.atm.domain.*;
import com.uni.atm.repo.CardRepo;
import com.uni.atm.requests.CardValidationRequest;
import com.uni.atm.requests.TransactionRequest;
import com.uni.atm.requests.TransactionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@WebMvcTest(CustomerController.class)
@Import(CustomerControllerTestConfig.class)
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CardRepo cardRepo;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
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
    }

    @Test
    public void validateCustomer() throws Exception {
        CardValidationRequest cardValidationRequest = new CardValidationRequest();
        cardValidationRequest.setCardNo("3496746711713539");
        cardValidationRequest.setPin("6604");

        this.mockMvc.perform(
                        MockMvcRequestBuilders.post("/customer-validate")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(cardValidationRequest))
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void shouldGet401ForWrongPingInValidateCustomer() throws Exception {
        CardValidationRequest cardValidationRequest = new CardValidationRequest();
        cardValidationRequest.setCardNo("3496746711713539");
        cardValidationRequest.setPin("0000");

        this.mockMvc.perform(
                        MockMvcRequestBuilders.post("/customer-validate")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(cardValidationRequest))
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Invalid Pin"));
    }

    @Test
    public void shouldGet404ForNotExistCard() throws Exception {
        CardValidationRequest cardValidationRequest = new CardValidationRequest();
        cardValidationRequest.setCardNo("1234567890123456");
        cardValidationRequest.setPin("0000");

        this.mockMvc.perform(
                        MockMvcRequestBuilders.post("/customer-validate")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(cardValidationRequest))
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Card Not Found"));
    }

    @Test
    public void performBalanceInquiryTransactionSuccessfully() throws Exception {
        TransactionRequest transactionRequest = new TransactionRequest();
        transactionRequest.setCardNo("3496746711713539");
        transactionRequest.setTransactionType(TransactionType.BALANCE_INQUIRY);
        transactionRequest.setPin("6604");

        this.mockMvc.perform(
                        MockMvcRequestBuilders.post("/customer-transaction")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(transactionRequest))
                ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.currentBalance").value(2000.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccessFull").value(true));
    }

    @Test
    public void performBalanceInquiryTransactionWrongPin() throws Exception {
        TransactionRequest transactionRequest = new TransactionRequest();
        transactionRequest.setCardNo("3496746711713539");
        transactionRequest.setTransactionType(TransactionType.BALANCE_INQUIRY);
        transactionRequest.setPin("0000");

        this.mockMvc.perform(
                        MockMvcRequestBuilders.post("/customer-transaction")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(transactionRequest))
                ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Invalid Pin"));

    }

    @Test
    public void performWithdrawTransactionSuccessfully() throws Exception {
        TransactionRequest transactionRequest = new TransactionRequest();
        transactionRequest.setCardNo("3496746711713539");
        transactionRequest.setTransactionType(TransactionType.WITHDRAW);
        transactionRequest.setAmount(BigDecimal.valueOf(1000.0));
        transactionRequest.setPin("6604");

        this.mockMvc.perform(
                        MockMvcRequestBuilders.post("/customer-transaction")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(transactionRequest))
                ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.currentBalance").value(1000.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccessFull").value(true));
    }

    @Test
    public void performWithdrawTransactionWithInsufficientBalance() throws Exception {
        TransactionRequest transactionRequest = new TransactionRequest();
        transactionRequest.setCardNo("3496746711713539");
        transactionRequest.setTransactionType(TransactionType.WITHDRAW);
        transactionRequest.setAmount(BigDecimal.valueOf(5000.0));
        transactionRequest.setPin("6604");

        this.mockMvc.perform(
                        MockMvcRequestBuilders.post("/customer-transaction")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(transactionRequest))
                ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Insufficient Balance"));
    }

    @Test
    public void performDepositTransactionSuccessfully() throws Exception {
        TransactionRequest transactionRequest = new TransactionRequest();
        transactionRequest.setCardNo("3496746711713539");
        transactionRequest.setTransactionType(TransactionType.DEPOSIT);
        transactionRequest.setAmount(BigDecimal.valueOf(5000.0));
        transactionRequest.setPin("6604");

        this.mockMvc.perform(
                        MockMvcRequestBuilders.post("/customer-transaction")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(transactionRequest))
                ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.currentBalance").value(7000.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccessFull").value(true));
    }
}
