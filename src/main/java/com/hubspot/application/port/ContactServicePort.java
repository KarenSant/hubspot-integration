package com.hubspot.application.port;

public interface ContactServicePort {
    String createContact(String firstName, String lastName, String email, String accessToken);
}