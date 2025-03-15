package com.hubspot.adapter.in;

import com.hubspot.config.HubspotConfig;
import com.hubspot.adapter.out.OAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/oauth")
public class OAuthController {

    private final HubspotConfig config;
    private final OAuthService oauthService;
    private static final Logger logger = LoggerFactory.getLogger(OAuthController.class);


    public OAuthController(HubspotConfig config, OAuthService oauthService) {
        this.config = config;
        this.oauthService = oauthService;
    }

    @GetMapping("/authorize")
    public String getAuthorizationUrl() {
        String authorizationUrl = config.getAuthUrl() + "?client_id=" + config.getClientId()
                + "&redirect_uri=" + config.getRedirectUri()
                + "&scope=crm.objects.contacts.write+crm.objects.contacts.read+oauth"
                + "&response_type=code";

        logger.info("Authorization URL gerada: {}", authorizationUrl);

        return authorizationUrl;
    }

    @GetMapping("/callback")
    public ResponseEntity<String> handleCallback(@RequestParam("code") String authorizationCode) {
        try {
            String accessToken = oauthService.exchangeAuthorizationCodeForToken(authorizationCode);

            logger.info("Access Token recebido: {}", accessToken);

            return ResponseEntity.ok("Access Token: " + accessToken);
        } catch (Exception e) {
            logger.error("Erro ao trocar código por token: {}", e.getMessage());
            return ResponseEntity.status(500).body("Erro ao trocar código pelo token: " + e.getMessage());
        }
    }
}
