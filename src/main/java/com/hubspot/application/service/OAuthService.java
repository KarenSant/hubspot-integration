package com.hubspot.application.service;

import com.hubspot.application.port.OAuthServicePort;
import com.hubspot.config.HubspotConfig;
import com.hubspot.domain.OAuthTokenResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OAuthService implements OAuthServicePort {

    private static final Logger logger = LoggerFactory.getLogger(OAuthService.class);
    private final HubspotConfig hubspotConfig;
    private final RestTemplate restTemplate;

    public OAuthService(HubspotConfig hubspotConfig, RestTemplate restTemplate) {
        this.hubspotConfig = hubspotConfig;
        this.restTemplate = restTemplate;
    }

    public String exchangeAuthorizationCodeForToken(String authorizationCode) {
        HttpHeaders headers = createHeaders();
        HttpEntity<String> request = createRequest(authorizationCode, headers);

        try {
            ResponseEntity<OAuthTokenResponse> response = restTemplate.exchange(
                    hubspotConfig.getTokenUrl(),
                    HttpMethod.POST,
                    request,
                    OAuthTokenResponse.class
            );

            OAuthTokenResponse tokenResponse = response.getBody();
            if (tokenResponse != null && tokenResponse.getAccess_token() != null) {
                return tokenResponse.getAccess_token();
            } else {
                throw new RuntimeException("Token de acesso não encontrado na resposta.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao trocar código pelo token: " + e.getMessage(), e);
        }
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return headers;
    }

    private HttpEntity<String> createRequest(String authorizationCode, HttpHeaders headers) {
        String requestBody = "grant_type=authorization_code" +
                "&client_id=" + hubspotConfig.getClientId() +
                "&client_secret=" + hubspotConfig.getClientSecret() +
                "&redirect_uri=" + hubspotConfig.getRedirectUri() +
                "&code=" + authorizationCode;

        return new HttpEntity<>(requestBody, headers);
    }
}