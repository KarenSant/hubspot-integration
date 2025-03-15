package com.hubspot.application.port;

public interface OAuthServicePort {
    String exchangeAuthorizationCodeForToken(String authorizationCode);
}