package com.generatedcreditcardchecker.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class CreditCardStatusResponse {

    private List<CreditCardResponse> creditCardStatus;

}
