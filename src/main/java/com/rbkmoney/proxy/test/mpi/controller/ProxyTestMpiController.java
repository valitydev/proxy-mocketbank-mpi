package com.rbkmoney.proxy.test.mpi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rbkmoney.proxy.test.mpi.model.Card;
import com.rbkmoney.proxy.test.mpi.utils.CardUtils;
import com.rbkmoney.proxy.test.mpi.utils.MpiAction;
import com.rbkmoney.proxy.test.mpi.utils.constant.MpiCavvAlgorithm;
import com.rbkmoney.proxy.test.mpi.utils.constant.MpiEnrollmentStatus;
import com.rbkmoney.proxy.test.mpi.utils.constant.MpiTransactionStatus;
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
public class ProxyTestMpiController {

    public final static String DATETIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    @Value("${fixture.cards}")
    private Resource fixtureCards;

    @Value("${proxy-test-mpi.callbackUrl}")
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

        CardUtils cardUtils = new CardUtils(cardList);
        Optional<Card> card = cardUtils.getCardByPan(pan);

        Map<String, String> map = new HashMap<>();

        map.put("enrolled", MpiEnrollmentStatus.CARDHOLDER_NOT_PARTICIPATING);
        if (cardUtils.isEnrolled(card)) {
            map.put("enrolled", MpiEnrollmentStatus.AUTHENTICATION_AVAILABLE);
            map.put("paReq", "paReq");
            map.put("acsUrl", proxyTestMpiCallbackUrl + "/mpi/acs");
        }

        return new ObjectMapper().writeValueAsString(map);
    }

    @RequestMapping(value = "validatePaRes", method = RequestMethod.POST)
    public String validatePaRes(
            @RequestParam(value = "pan", required = true) String pan,
            @RequestParam(value = "paRes", required = true) String paRes
    ) throws IOException {

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

        return new ObjectMapper().writeValueAsString(map);
    }

    @RequestMapping(value = "acs", method = RequestMethod.POST)
    public ModelAndView formAcs(
            @RequestParam(value = "PaReq", required = true) String paReq,
            @RequestParam(value = "MD", required = true) String md,
            @RequestParam(value = "TermUrl", required = true) String termUrl
    ) {
        ModelAndView model = new ModelAndView();
        model.setViewName("acs_form");
        model.addObject("action", termUrl);
        model.addObject("pan", "XXXX XXXX XXXX XXXX");
        model.addObject("PaRes", "PaRes");
        model.addObject("MD", md);
        return model;
    }

}
