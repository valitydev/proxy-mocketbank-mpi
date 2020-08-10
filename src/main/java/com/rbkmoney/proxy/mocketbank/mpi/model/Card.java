package com.rbkmoney.proxy.mocketbank.mpi.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Card {

    private String pan;
    private String action;
    private String paymentSystem;

}
