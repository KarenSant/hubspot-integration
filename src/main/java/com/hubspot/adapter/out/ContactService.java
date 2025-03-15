package com.hubspot.adapter.out;

import com.hubspot.application.port.ContactServicePort;
import com.hubspot.config.HubspotConfig;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
        String url = hubspotConfig.getApiUrl() + "/crm/v3/objects/contacts";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> contactData = new HashMap<>();
        Map<String, String> properties = new HashMap<>();
        properties.put("firstname", firstName);
        properties.put("lastname", lastName);
        properties.put("email", email);
        contactData.put("properties", properties);
        logger.info("Enviando requisição para criar contato: {}", properties);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(contactData, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        logger.debug("Resposta da API HubSpot: {}", response.getBody());
        return response.getBody();
    }
}
