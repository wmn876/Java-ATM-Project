package com.uni.atm.requests;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRequest {
    
    @NotEmpty
    private String firstName;
    private String lastName;

    private BigDecimal initialBalance;
}
