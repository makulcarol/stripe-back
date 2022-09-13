package com.generatedcreditcardchecker.request;

import lombok.Data;

@Data
public class GeneratedCreditCardInfo {

    private String cardNumber;

    private String expMonth;

    private String expYear;

    private String securityCode;

}
