package com.rbkmoney.proxy.mocketbank.mpi.utils;

import net.logstash.logback.encoder.org.apache.commons.lang.StringUtils;


public class MpiUtils {

    public final static String MASK_CHAR = "*";

    public static String maskNumber(final String creditCardNumber, int startLength, int endLength, String maskChar) {
        final String cardNumber = creditCardNumber.replaceAll("\\D", "");

        final int start = startLength;
        final int end = cardNumber.length() - endLength;
        final String overlay = StringUtils.repeat(maskChar, end - start);

        return StringUtils.overlay(cardNumber, overlay, start, end);
    }

    public static String maskNumber(final String creditCardNumber) {
        return maskNumber(creditCardNumber, 4, 4, MASK_CHAR);
    }

}
