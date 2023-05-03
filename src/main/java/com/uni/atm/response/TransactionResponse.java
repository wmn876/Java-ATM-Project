package com.uni.atm.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {

    @JsonProperty("isSuccessFull")
    private boolean isSuccessFull;

    private BigDecimal currentBalance;
}
