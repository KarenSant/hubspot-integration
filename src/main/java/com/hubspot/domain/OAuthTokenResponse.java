package com.hubspot.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OAuthTokenResponse {

    private String access_token;
    private String refresh_token;
    private String scope;
    private String token_type;
}
