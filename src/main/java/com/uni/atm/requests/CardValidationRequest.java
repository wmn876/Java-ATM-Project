package com.uni.atm.requests;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class CardValidationRequest {
     
     @NotEmpty(message = "card number is required")
     @Size(max = 16, min = 16, message = "card no must have 16 digits")
     private String cardNo;
     
     @NotEmpty(message = "pin is required")
     @Size(max = 4, min = 4, message = "pin must have 4 digits")
     private String pin;
}