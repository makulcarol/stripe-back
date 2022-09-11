package com.generatedcreditcardchecker.controller;



import com.generatedcreditcardchecker.request.GeneratedCreditCardRequest;
import com.generatedcreditcardchecker.service.CheckService;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/stripe")
@RestController
public class CheckController {


    public final CheckService checkService;

    @Lazy
    public CheckController(CheckService checkService) {
        this.checkService = checkService;
    }


    @PostMapping(value = "/check",consumes={MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> checkCreditCard(@RequestBody GeneratedCreditCardRequest generatedCreditCardRequest) {
        checkService.checkCreditCardGenerated(generatedCreditCardRequest);

        return new ResponseEntity<>(HttpStatus.OK);
    }


}
