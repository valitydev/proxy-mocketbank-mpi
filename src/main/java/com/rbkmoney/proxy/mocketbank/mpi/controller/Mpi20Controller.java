package com.rbkmoney.proxy.mocketbank.mpi.controller;

import com.rbkmoney.proxy.mocketbank.mpi.handler.mpi20.CardHandler;
import com.rbkmoney.proxy.mocketbank.mpi.model.mpi20.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

import static com.rbkmoney.proxy.mocketbank.mpi.utils.constant.MpiV2RequestParams.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/mpi20")
public class Mpi20Controller {

    private final List<CardHandler> cardHandlers;

    @PostMapping(value = "/prepare")
    public PreparationResponse prepare(@RequestBody PreparationRequest preparationRequest) {
        return cardHandlers.stream()
                .filter(c -> c.isHandle(preparationRequest.getPan()))
                .findFirst()
                .map(c -> c.prepareHandle(preparationRequest))
                .orElseThrow();
    }

    @PostMapping(value = "/auth")
    public AuthenticationResponse auth(@RequestBody AuthenticationRequest authenticationRequest) {
        return cardHandlers.stream()
                .filter(c -> c.isHandle(authenticationRequest.getPan()))
                .findFirst()
                .map(c -> c.authHandle(authenticationRequest))
                .orElseThrow();
    }

    @PostMapping(value = "/result")
    public ResultResponse result(@RequestBody ResultRequest resultRequest) {
        return cardHandlers.stream()
                .filter(c -> c.isHandle(resultRequest))
                .findFirst()
                .map(c -> c.resultHandle(resultRequest))
                .orElseThrow();
    }

    @RequestMapping(value = "/three_ds_method", method = RequestMethod.POST)
    public ModelAndView threeDsMethod(@RequestParam(value = THREE_DS_METHOD_DATA) String threeDSMethodData,
                                      @RequestParam(value = TERM_URL) String termUrl,
                                      @RequestParam(value = TERMINATION_URI) String terminationUri) {
        log.info("Form threeDsMethod 2.0 input params: threeDSMethodData {}, termUrl {}, terminationUri {}",
                threeDSMethodData, termUrl, terminationUri);
        ModelAndView model = new ModelAndView();
        model.setViewName("threeDsMethod_2.0_form");
        model.addObject(ACTION, UriComponentsBuilder.fromUriString(termUrl)
                .queryParam(TERMINATION_URI, terminationUri)
                .build()
                .toUriString());
        model.addObject(THREE_DS_METHOD_DATA, threeDSMethodData);
        model.addObject(TERMINATION_URI, terminationUri);
        log.info("Form threeDsMethod 2.0 show the form");
        return model;
    }

    @RequestMapping(value = "/acs", method = RequestMethod.POST)
    public ModelAndView acs(@RequestParam(value = CREQ) String creq,
                            @RequestParam(value = TERM_URL) String termUrl,
                            @RequestParam(value = TERMINATION_URI) String terminationUri) {
        log.info("Form ACS 2.0 input params: creq {}, termUrl {}, terminationUri {}", creq, termUrl, terminationUri);
        ModelAndView model = new ModelAndView();
        model.setViewName("acs_2.0_form");
        model.addObject(ACTION, UriComponentsBuilder.fromUriString(termUrl)
                .queryParam(TERMINATION_URI, terminationUri)
                .build()
                .toUriString());
        model.addObject(PAN, "XXXX XXXX XXXX XXXX");
        model.addObject(CREQ, creq);
        model.addObject(TERMINATION_URI, terminationUri);
        log.info("Form ACS 2.0 show the form");
        return model;
    }

}
