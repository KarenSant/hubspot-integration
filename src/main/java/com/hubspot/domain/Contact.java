package com.hubspot.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Contact {
    private String firstName;
    private String lastName;
    private String email;

    public Contact() {
    }

    public Contact(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
}
