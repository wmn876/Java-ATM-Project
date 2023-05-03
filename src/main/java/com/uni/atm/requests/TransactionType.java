package com.uni.atm.requests;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum TransactionType {


    WITHDRAW,
    DEPOSIT,
    BALANCE_INQUIRY;

    public static List<String> supportedTransactionType() {
        return Arrays.stream(TransactionType.values()).map(Enum::name).collect(Collectors.toList());
    }
}
