package com.hubspot.application.service;

import com.hubspot.application.port.ContactServicePort;
import com.hubspot.config.HubspotConfig;
import com.hubspot.domain.Contact;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
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
    public String createContact(Contact contact, String accessToken) {
        logger.info("Enviando contato para HubSpot: {}", contact.getEmail());
        String url = hubspotConfig.getApiUrl() + "/crm/v3/objects/contacts";

        HttpHeaders headers = createHeaders(accessToken);
        HttpEntity<Map<String, Object>> request = createRequest(contact, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
        logger.info("Contato criado com sucesso: {}", response.getBody());
        return response.getBody();
    }

    @Override
    public List<Contact> getAllContacts(String accessToken) {
        logger.info("Listando todos os contatos do HubSpot.");
        String url = hubspotConfig.getApiUrl() + "/crm/v3/objects/contacts";

        HttpHeaders headers = createHeaders(accessToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                request,
                new ParameterizedTypeReference<Map<String, Object>>() {
                }
        );

        List<Contact> contacts = new ArrayList<>();
        if (response.getBody() != null && response.getBody().containsKey("results")) {
            List<Map<String, Object>> results = (List<Map<String, Object>>) response.getBody().get("results");
            for (Map<String, Object> result : results) {
                Map<String, Object> properties = (Map<String, Object>) result.get("properties");
                Contact contact = new Contact();
                contact.setFirstName((String) properties.get("firstname"));
                contact.setLastName((String) properties.get("lastname"));
                contact.setEmail((String) properties.get("email"));
                contacts.add(contact);
            }
        }

        logger.info("Contatos listados com sucesso.");
        return contacts;
    }

    private HttpHeaders createHeaders(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);
        return headers;
    }

    private HttpEntity<Map<String, Object>> createRequest(Contact contact, HttpHeaders headers) {
        Map<String, Object> contactData = Map.of(
                "properties", Map.of(
                        "firstname", contact.getFirstName(),
                        "lastname", contact.getLastName(),
                        "email", contact.getEmail()
                )
        );

        logger.debug("Corpo da requisição: {}", contactData);
        return new HttpEntity<>(contactData, headers);
    }
}