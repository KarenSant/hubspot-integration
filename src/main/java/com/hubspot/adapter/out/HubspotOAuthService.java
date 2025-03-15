//package com.hubspot.adapter.out;
//
//import com.hubspot.application.port.OAuthServicePort;
//import com.hubspot.config.HubspotConfig;
//import com.hubspot.domain.OAuthTokenResponse;
//import org.springframework.http.*;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//
//@Service
//public class HubspotOAuthService implements OAuthServicePort {
//
//    private final RestTemplate restTemplate;
//    private final HubspotConfig hubspotConfig;
//    private static final String CREATE_CONTACT_URL = "https://api.hubapi.com/contacts/v1/contact?hapikey=";
//
//    public HubspotOAuthService(RestTemplate restTemplate, HubspotConfig hubspotConfig) {
//        this.hubspotConfig = hubspotConfig;
//        this.restTemplate = restTemplate;
//    }
//
//    @Override
//    public String exchangeAuthorizationCodeForToken(String authorizationCode) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//
//        String requestBody = "grant_type=authorization_code" +
//                "&client_id=" + hubspotConfig.getClientId() +
//                "&client_secret=" + hubspotConfig.getClientSecret() +
//                "&redirect_uri=" + hubspotConfig.getRedirectUri() +
//                "&code=" + authorizationCode;
//
//        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
//
//        ResponseEntity<OAuthTokenResponse> response = restTemplate.exchange(
//                hubspotConfig.getTokenUrl(),
//                HttpMethod.POST,
//                request,
//                OAuthTokenResponse.class
//        );
//
//        if (response.getBody() != null && response.getBody().getAccess_token() != null) {
//            return response.getBody().getAccess_token();
//        } else {
//            throw new RuntimeException("Token de acesso não encontrado na resposta.");
//        }
//    }
//
//}