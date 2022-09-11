package com.generatedcreditcardchecker.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CreditCardResponse {

    private String creditCard;

    private boolean isOk;

    private String decline_code;

    private String code;

    private String message;

    private String type;

}
