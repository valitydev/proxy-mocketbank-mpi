package com.rbkmoney.proxy.mocketbank.mpi.handler.mpi20;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rbkmoney.proxy.mocketbank.mpi.configuration.properties.Mpi20Properties;
import com.rbkmoney.proxy.mocketbank.mpi.model.Card;
import com.rbkmoney.proxy.mocketbank.mpi.model.mpi20.*;
import com.rbkmoney.proxy.mocketbank.mpi.utils.MpiAction;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.UUID;

@Configuration
@RequiredArgsConstructor
public class SuccessCardHandler implements CardHandler {

    private final List<Card> cardList;
    private final Mpi20Properties mpi20Properties;
    private final ObjectMapper objectMapper;

    @Override
    public boolean isHandle(String pan) {
        return cardList.stream()
                .anyMatch(c -> c.getPan().equals(pan)
                        && c.getAction().equals(MpiAction.THREE_D_SECURE_2_0_SUCCESS.getAction()));
    }

    @Override
    public boolean isHandle(ResultRequest request) {
        return request.getThreeDSServerTransID().startsWith(MpiAction.THREE_D_SECURE_2_0_SUCCESS.getAction());
    }

    @SneakyThrows
    @Override
    public PreparationResponse prepareHandle(PreparationRequest request) {
        String threeDSServerTransID = buildTransId();
        return PreparationResponse.builder()
                .threeDSServerTransID(threeDSServerTransID)
                .protocolVersion("2")
                .threeDSMethodURL(mpi20Properties.getCallbackUrl() + mpi20Properties.getPathThreeDsMethodUrl())
                .threeDSMethodData(objectMapper.writeValueAsString(new ThreeDSMethodData(threeDSServerTransID)))
                .build();
    }

    @SneakyThrows
    @Override
    public AuthenticationResponse authHandle(AuthenticationRequest request) {
        return AuthenticationResponse.builder()
                .threeDSServerTransID(request.getThreeDSServerTransID())
                .transStatus(TransactionStatus.CHALLENGE_REQUIRED.getCode())
                .acsUrl(mpi20Properties.getCallbackUrl() + mpi20Properties.getPathAcsUrl())
                .creq(objectMapper.writeValueAsString(new CReq(request.getThreeDSServerTransID())))
                .build();
    }

    @Override
    public ResultResponse resultHandle(ResultRequest request) {
        return ResultResponse.builder()
                .transactionId(request.getThreeDSServerTransID())
                .transStatus(TransactionStatus.AUTHENTICATION_SUCCESSFUL.getCode())
                .build();
    }

    private String buildTransId() {
        return MpiAction.THREE_D_SECURE_2_0_SUCCESS.getAction() + UUID.randomUUID().toString();
    }
}
