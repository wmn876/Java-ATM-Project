package com.uni.atm.requests;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Data
public class TransactionRequest {

    @NotEmpty(message = "card number is required")
    @Size(max = 16, min = 16, message = "card no must have 16 digits")
    private String cardNo;
    @NotNull(message = "transaction type must be selected")
    private TransactionType transactionType;

    private BigDecimal amount;
    @NotEmpty(message = "pin is required")
    @Size(max = 4, min = 4, message = "pin must have 4 digits")
    private String pin;
}