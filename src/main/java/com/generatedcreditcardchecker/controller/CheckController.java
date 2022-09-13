package com.generatedcreditcardchecker.controller;

import com.generatedcreditcardchecker.request.GeneratedCreditCardRequest;
import com.generatedcreditcardchecker.service.CheckService;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RequestMapping("/stripe")
@RestController
public class CheckController {

    public final CheckService checkService;

    @Lazy
    public CheckController(CheckService checkService) {
        this.checkService = checkService;
    }

    @CrossOrigin
    @PostMapping(value = "/check", consumes = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<Object> checkCreditCard(@RequestBody GeneratedCreditCardRequest generatedCreditCardRequest) {
        try {
            checkService.checkCreditCardGenerated(generatedCreditCardRequest);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception exc) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, exc.getMessage(), exc);
        }
    }

}
