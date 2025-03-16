package com.hubspot.adapter.in;

import com.hubspot.application.port.ContactServicePort;
import com.hubspot.config.HubspotConfig;
import com.hubspot.domain.Contact;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/contacts")
public class ContactController {

    private static final Logger logger = LoggerFactory.getLogger(ContactController.class);
    private final HubspotConfig hubspotConfig;
    private final RestTemplate restTemplate;
    private final ContactServicePort contactServicePort;

    public ContactController(ContactServicePort contactServicePort) {
        this.hubspotConfig = new HubspotConfig();
        this.contactServicePort = contactServicePort;
        this.restTemplate = new RestTemplate();
    }

    @PostMapping("/create")
    public ResponseEntity<String> createContact(@RequestBody Contact contact,
                                                @RequestHeader("Authorization") String authHeader) {
        logger.info("Recebendo solicitação para criar contato: {}", contact);
        if (contact.getFirstName() == null || contact.getLastName() == null || contact.getEmail() == null) {
            logger.error("Dados incompletos para criar o contato: {}", contact);
            return ResponseEntity.badRequest().body("Todos os campos (firstName, lastName, email) são obrigatórios.");
        }

        String accessToken = authHeader.replace("Bearer ", "");
        String response = contactServicePort.createContact(contact, accessToken);

        logger.info("Contato criado com sucesso no HubSpot.");
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<Contact>> getAllContacts(@RequestHeader("Authorization") String authHeader) {
        logger.info("Recebendo solicitação para listar todos os contatos.");

        String accessToken = authHeader.replace("Bearer ", "");
        List<Contact> contacts = contactServicePort.getAllContacts(accessToken);

        logger.info("Contatos listados com sucesso.");
        return ResponseEntity.ok(contacts);
    }


}
