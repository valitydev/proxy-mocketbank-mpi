package com.rbkmoney.proxy.mocketbank.mpi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rbkmoney.proxy.mocketbank.mpi.model.Card;
import com.rbkmoney.proxy.mocketbank.mpi.utils.CardUtils;
import com.rbkmoney.proxy.mocketbank.mpi.utils.MpiAction;
import com.rbkmoney.proxy.mocketbank.mpi.utils.MpiUtils;
import com.rbkmoney.proxy.mocketbank.mpi.utils.constant.MpiCavvAlgorithm;
import com.rbkmoney.proxy.mocketbank.mpi.utils.constant.MpiEnrollmentStatus;
import com.rbkmoney.proxy.mocketbank.mpi.utils.constant.MpiTransactionStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping(value = "/mpi")
public class MpiController {

    private final static Logger LOGGER = LoggerFactory.getLogger(MpiController.class);

    public final static String DATETIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    @Value("${fixture.cards}")
    private Resource fixtureCards;

    @Value("${proxy-mocketbank-mpi.callbackUrl}")
    private Resource proxyTestMpiCallbackUrl;

    List<Card> cardList;

    @PostConstruct
    public void init() throws IOException {
        cardList = CardUtils.getCardListFromFile(fixtureCards.getInputStream());
    }

    @RequestMapping(value = "verifyEnrollment", method = RequestMethod.POST)
    public String verifyEnrollment(
            @RequestParam(value = "pan", required = true) String pan,
            @RequestParam(value = "year", required = true) String year,
            @RequestParam(value = "month", required = true) String month
    ) throws IOException {
        LOGGER.info("VerifyEnrollment input params: pan {}, year {}, month {}",
                MpiUtils.maskNumber(pan), year, month
        );

        CardUtils cardUtils = new CardUtils(cardList);
        Optional<Card> card = cardUtils.getCardByPan(pan);

        Map<String, String> map = new HashMap<>();

        map.put("enrolled", MpiEnrollmentStatus.CARDHOLDER_NOT_PARTICIPATING);
        if (cardUtils.isEnrolled(card)) {
            map.put("enrolled", MpiEnrollmentStatus.AUTHENTICATION_AVAILABLE);
            map.put("paReq", "paReq");
            map.put("acsUrl", proxyTestMpiCallbackUrl + "/mpi/acs");
        }

        String response = new ObjectMapper().writeValueAsString(map);
        LOGGER.info("VerifyEnrollment response {}", response);

        return response;
    }

    @RequestMapping(value = "validatePaRes", method = RequestMethod.POST)
    public String validatePaRes(
            @RequestParam(value = "pan", required = true) String pan,
            @RequestParam(value = "paRes", required = true) String paRes
    ) throws IOException {

        LOGGER.info("ValidatePaRes input params: pan {}, paRes {}", MpiUtils.maskNumber(pan), paRes);

        CardUtils cardUtils = new CardUtils(cardList);
        Optional<Card> card = cardUtils.getCardByPan(pan);
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

        String response = new ObjectMapper().writeValueAsString(map);
        LOGGER.info("ValidatePaRes response {}", response);

        return response;
    }

    @RequestMapping(value = "acs", method = RequestMethod.POST)
    public ModelAndView formAcs(
            @RequestParam(value = "PaReq", required = true) String paReq,
            @RequestParam(value = "MD", required = true) String md,
            @RequestParam(value = "TermUrl", required = true) String termUrl
    ) {
        LOGGER.info("Form ACS input params: paReq {}, MD {}, TermUrl {}", paReq, md, termUrl);
        ModelAndView model = new ModelAndView();
        model.setViewName("acs_form");
        model.addObject("action", termUrl);
        model.addObject("pan", "XXXX XXXX XXXX XXXX");
        model.addObject("PaRes", "PaRes");
        model.addObject("MD", md);
        LOGGER.info("Form ACS show the form");

        return model;
    }

}
