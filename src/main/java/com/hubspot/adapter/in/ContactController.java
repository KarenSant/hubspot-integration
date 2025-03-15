package com.hubspot.adapter.in;

import com.hubspot.application.port.ContactServicePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/contacts")
public class ContactController {

    private static final Logger logger = LoggerFactory.getLogger(ContactController.class);
    private final ContactServicePort contactServicePort;

    public ContactController(ContactServicePort contactServicePort) {
        this.contactServicePort = contactServicePort;
    }

    @PostMapping
    public ResponseEntity<String> createContact(@RequestBody Map<String, String> contactData,
                                                @RequestHeader("Authorization") String authHeader) {
        logger.info("Recebendo solicitação para criar contato: {}", contactData);

        String accessToken = authHeader.replace("Bearer ", "");
        String response = contactServicePort.createContact(
                contactData.get("firstname"),
                contactData.get("lastname"),
                contactData.get("email"),
                accessToken
        );

        logger.info("Contato criado com sucesso no HubSpot.");
        return ResponseEntity.ok(response);
    }
}
