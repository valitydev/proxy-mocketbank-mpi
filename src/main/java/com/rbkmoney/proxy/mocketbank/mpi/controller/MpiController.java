package com.rbkmoney.proxy.mocketbank.mpi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rbkmoney.proxy.mocketbank.mpi.configuration.properties.AdapterProperties;
import com.rbkmoney.proxy.mocketbank.mpi.model.Card;
import com.rbkmoney.proxy.mocketbank.mpi.utils.CardUtils;
import com.rbkmoney.proxy.mocketbank.mpi.utils.MpiAction;
import com.rbkmoney.proxy.mocketbank.mpi.utils.MpiUtils;
import com.rbkmoney.proxy.mocketbank.mpi.utils.UrlUtils;
import com.rbkmoney.proxy.mocketbank.mpi.utils.constant.MpiCavvAlgorithm;
import com.rbkmoney.proxy.mocketbank.mpi.utils.constant.MpiEnrollmentStatus;
import com.rbkmoney.proxy.mocketbank.mpi.utils.constant.MpiTransactionStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/mpi")
public class MpiController {

    private static final String DATETIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    private final ObjectMapper objectMapper;
    private final AdapterProperties properties;
    private final List<Card> cardList;

    @RequestMapping(value = "verifyEnrollment", method = RequestMethod.POST)
    public String verifyEnrollment(
            @RequestParam(value = "pan") String pan,
            @RequestParam(value = "year") String year,
            @RequestParam(value = "month") String month
    ) throws IOException {
        log.info("VerifyEnrollment input params: pan {}, year {}, month {}",
                MpiUtils.maskNumber(pan), year, month
        );

        Optional<Card> card = CardUtils.extractCardByPan(cardList, pan);

        Map<String, String> map = new HashMap<>();
        map.put("enrolled", MpiEnrollmentStatus.CARDHOLDER_NOT_PARTICIPATING);
        if (CardUtils.isEnrolled(card)) {
            map.put("enrolled", MpiEnrollmentStatus.AUTHENTICATION_AVAILABLE);
            map.put("paReq", "paReq");
            map.put("acsUrl", UrlUtils.prepareCallbackUrl(properties.getCallbackUrl(), properties.getPathAcsUrl()));
        }

        String response = objectMapper.writeValueAsString(map);
        log.info("VerifyEnrollment response {}", response);
        return response;
    }

    @RequestMapping(value = "validatePaRes", method = RequestMethod.POST)
    public String validatePaRes(
            @RequestParam(value = "pan") String pan,
            @RequestParam(value = "paRes") String paRes
    ) throws IOException {

        log.info("ValidatePaRes input params: pan {}, paRes {}", MpiUtils.maskNumber(pan), paRes);
        Optional<Card> card = CardUtils.extractCardByPan(cardList, pan);
        Map<String, String> map = new HashMap<>();
        if (card.isPresent()) {
            MpiAction action = MpiAction.findByValue(card.get().getAction());
            switch (action) {
                case THREE_D_SECURE_SUCCESS:
                    map.put("transactionStatus", MpiTransactionStatus.AUTHENTICATION_SUCCESSFUL);
                    map.put("eci", "1");
                    map.put("cavv", "3");
                    map.put("cavvAlgorithm", MpiCavvAlgorithm.CVV_WITH_ATN);
                    map.put("txId", UUID.randomUUID().toString());
                    map.put("txTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATETIME_PATTERN)));
                    break;
                case THREE_D_SECURE_FAILURE:
                    map.put("transactionStatus", MpiTransactionStatus.AUTHENTICATION_FAILED);
                    break;
                default:
                    map.put("transactionStatus", MpiTransactionStatus.ATTEMPTS_PROCESSING_PERFORMED);
            }
        } else {
            map.put("transactionStatus", MpiTransactionStatus.AUTHENTICATION_COULD_NOT_BE_PERFORMED);
        }

        String response = objectMapper.writeValueAsString(map);
        log.info("ValidatePaRes response {}", response);
        return response;
    }

    @RequestMapping(value = "acs", method = RequestMethod.POST)
    public ModelAndView formAcs(
            @RequestParam(value = "PaReq") String paReq,
            @RequestParam(value = "MD") String md,
            @RequestParam(value = "TermUrl") String termUrl
    ) {
        log.info("Form ACS input params: paReq {}, MD {}, TermUrl {}", paReq, md, termUrl);
        ModelAndView model = new ModelAndView();
        model.setViewName("acs_form");
        model.addObject("action", termUrl);
        model.addObject("pan", "XXXX XXXX XXXX XXXX");
        model.addObject("PaRes", "PaRes");
        model.addObject("MD", md);
        log.info("Form ACS show the form");
        return model;
    }

}
