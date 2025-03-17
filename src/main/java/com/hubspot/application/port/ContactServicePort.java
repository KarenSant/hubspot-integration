package com.hubspot.application.port;

import com.hubspot.domain.Contact;

import java.util.List;


public interface ContactServicePort {
    String createContact(Contact contact, String accessToken);

    List<Contact> getAllContacts(String accessToken);
}