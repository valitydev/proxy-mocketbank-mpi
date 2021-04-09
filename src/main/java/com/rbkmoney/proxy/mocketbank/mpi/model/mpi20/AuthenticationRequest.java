package com.rbkmoney.proxy.mocketbank.mpi.model.mpi20;

import lombok.Data;
import lombok.ToString;

@Data
public class AuthenticationRequest {
    private String threeDSServerTransID;

    @ToString.Exclude
    private String pan;
    private String cardholderName;
    @ToString.Exclude
    private String expDate;

    private String notificationUrl;
    private String amount;
    private String currency;

}
