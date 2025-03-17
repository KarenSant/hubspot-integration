package com.hubspot.adapter.in;

import com.hubspot.application.service.OAuthService;
import com.hubspot.config.HubspotConfig;
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

    private static final Logger logger = LoggerFactory.getLogger(OAuthController.class);
    private final HubspotConfig config;
    private final OAuthService oauthService;

    public OAuthController(HubspotConfig config, OAuthService oauthService) {
        this.config = config;
        this.oauthService = oauthService;
    }

    @GetMapping("/authorize")
    public ResponseEntity<String> getAuthorizationUrl() {
        String url = config.getAuthUrl() + "?client_id=" + config.getClientId()
                + "&redirect_uri=" + config.getRedirectUri()
                + "&scope=crm.objects.contacts.write+crm.objects.contacts.read+oauth"
                + "&response_type=code";

        logger.info("🔗 URL de autorização gerada: {}", url);
        return ResponseEntity.ok(url);
    }

    @GetMapping("/callback")
    public ResponseEntity<String> handleCallback(@RequestParam("code") String authorizationCode) {
        logger.info("🔄 Recebendo código de autorização...");
        String accessToken = oauthService.exchangeAuthorizationCodeForToken(authorizationCode);
        logger.info("✅ Token recebido com sucesso!");

        return ResponseEntity.ok("Access Token: " + accessToken);
    }
}
