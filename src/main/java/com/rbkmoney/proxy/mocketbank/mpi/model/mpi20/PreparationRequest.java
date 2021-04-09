package com.rbkmoney.proxy.mocketbank.mpi.model.mpi20;

import lombok.*;

@Data
public class PreparationRequest {
    @ToString.Exclude
    private String pan;
    private String notificationUrl;
}
