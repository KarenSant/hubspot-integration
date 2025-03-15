package com.hubspot.application.service;

import com.hubspot.application.port.ContactServicePort;
import com.hubspot.config.HubspotConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class ContactService implements ContactServicePort {

    private static final Logger logger = LoggerFactory.getLogger(ContactService.class);
    private final HubspotConfig hubspotConfig;
    private final RestTemplate restTemplate;

    public ContactService(HubspotConfig hubspotConfig, RestTemplate restTemplate) {
        this.hubspotConfig = hubspotConfig;
        this.restTemplate = restTemplate;
    }

    @Override
    public String createContact(String firstName, String lastName, String email, String accessToken) {
        logger.info("Enviando contato para HubSpot: {}", email);
        String url = hubspotConfig.getApiUrl() + "/crm/v3/objects/contacts";

        HttpHeaders headers = createHeaders(accessToken);
        HttpEntity<Map<String, Object>> request = createRequest(firstName, lastName, email, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
        logger.info("Contato criado com sucesso: {}", response.getBody());
        return response.getBody();
    }

    private HttpHeaders createHeaders(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);
        return headers;
    }

    private HttpEntity<Map<String, Object>> createRequest(String firstName, String lastName, String email,
            HttpHeaders headers) {
        Map<String, Object> contactData = new HashMap<>();
        Map<String, Object> properties = new HashMap<>();
        properties.put("firstname", firstName);
        properties.put("lastname", lastName);
        properties.put("email", email);
        contactData.put("properties", properties);
        return new HttpEntity<>(contactData, headers);
    }
}
