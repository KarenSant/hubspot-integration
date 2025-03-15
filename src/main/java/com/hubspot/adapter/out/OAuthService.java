package com.hubspot.adapter.out;

import com.hubspot.config.HubspotConfig;
import com.hubspot.domain.OAuthTokenResponse;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OAuthService {

    private final HubspotConfig hubspotConfig;
    private final RestTemplate restTemplate;

    public OAuthService(HubspotConfig hubspotConfig, RestTemplate restTemplate) {
        this.hubspotConfig = hubspotConfig;
        this.restTemplate = restTemplate;
    }

    public String exchangeAuthorizationCodeForToken(String authorizationCode) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String requestBody = "grant_type=authorization_code" +
                "&client_id=" + hubspotConfig.getClientId() +
                "&client_secret=" + hubspotConfig.getClientSecret() +
                "&redirect_uri=" + hubspotConfig.getRedirectUri() +
                "&code=" + authorizationCode;

        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

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
}