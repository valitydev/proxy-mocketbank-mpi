package com.rbkmoney.proxy.mocketbank.mpi.model.mpi20;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.*;

@Data
@Builder
public class AuthenticationResponse {

    private String threeDSServerTransID;

    @JsonUnwrapped
    private Error error;

    private String transStatus;

    private String acsUrl;

    private String creq;

    private String terminationUri;

}
