package com.rbkmoney.proxy.mocketbank.mpi.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MpiUtilsTest {

    @Test
    public void maskNumber() {
        assertEquals("4242********8989", MpiUtils.maskNumber("4242545467678989"));
        assertEquals("4253*********8449", MpiUtils.maskNumber("42535444557678449"));
    }

}
