package com.rbkmoney.proxy.mocketbank.mpi.controller;

import com.rbkmoney.proxy.mocketbank.mpi.handler.mpi20.CardHandler;
import com.rbkmoney.proxy.mocketbank.mpi.model.mpi20.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/mpi20")
public class Mpi20Controller {

    private final List<CardHandler> cardHandlers;

    @Value("${static.context.root}")
    private String staticContextRoot;

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
    public ModelAndView threeDsMethod(@RequestParam(value = "threeDSMethodData") String threeDSMethodData,
                                      @RequestParam(value = "TermUrl") String termUrl) {
        log.info("Form threeDsMethod 2.0 input params: threeDSMethodData {}, termUrl {}", threeDSMethodData, termUrl);
        ModelAndView model = new ModelAndView();
        model.setViewName("threeDsMethod_2.0_form");
        model.addObject("action", termUrl);
        model.addObject("threeDSMethodData", threeDSMethodData);
        addStaticContextRoot(model);
        log.info("Form threeDsMethod 2.0 show the form");
        return model;
    }

    @RequestMapping(value = "/acs", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView acs(@RequestParam(value = "creq") String creq,
                            @RequestParam(value = "TermUrl") String termUrl) {
        log.info("Form ACS 2.0 input params: creq {}, termUrl {}", creq, termUrl);
        ModelAndView model = new ModelAndView();
        model.setViewName("acs_2.0_form");
        model.addObject("action", termUrl);
        model.addObject("pan", "XXXX XXXX XXXX XXXX");
        model.addObject("cres", creq);
        addStaticContextRoot(model);
        log.info("Form ACS 2.0 show the form");
        return model;
    }

    private void addStaticContextRoot(ModelAndView model) {
        model.addObject("staticContextRoot", staticContextRoot);
    }

}
