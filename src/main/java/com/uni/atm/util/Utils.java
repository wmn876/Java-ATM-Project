package com.uni.atm.util;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.tuple.Pair;

public class Utils {

    public synchronized static Pair<String, String> generateCardNumberWithPin() {
        return Pair.of(RandomStringUtils.random(16, false, true), RandomStringUtils.random(4, false, true));
    }

    public synchronized static String generateAccountNumber() {
        return RandomStringUtils.random(17, false, true);
    }

}
