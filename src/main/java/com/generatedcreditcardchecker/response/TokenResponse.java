package com.generatedcreditcardchecker.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TokenResponse {

    private String id;

    private String client_ip;

}
