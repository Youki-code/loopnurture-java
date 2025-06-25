package org.springframework.samples.loopnurture.users.service.oauth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * 通过调用 https://oauth2.googleapis.com/tokeninfo?id_token=xxx 验证 Google ID Token。
 */
@Component
public class GoogleTokenValidator {

    private static final String TOKEN_INFO_URL = "https://oauth2.googleapis.com/tokeninfo?id_token={token}";

    private final RestTemplate restTemplate = new RestTemplate();
    private final String clientId;

    public GoogleTokenValidator(@Value("${google.oauth.client-id}") String clientId) {
        this.clientId = clientId;
    }

    public Payload validate(String idTokenString) {
        try {
            ResponseEntity<Map> resp = restTemplate.getForEntity(TOKEN_INFO_URL, Map.class, idTokenString);
            Map<String, Object> body = resp.getBody();
            if (body == null || body.get("aud") == null || !clientId.equals(body.get("aud"))) {
                throw new IllegalArgumentException("Google ID token audience mismatch");
            }
            if (!"true".equals(String.valueOf(body.get("email_verified")))) {
                throw new IllegalArgumentException("Email not verified by Google");
            }
            return new Payload(
                    String.valueOf(body.get("sub")),
                    String.valueOf(body.get("email")),
                    String.valueOf(body.getOrDefault("name", "")),
                    String.valueOf(body.getOrDefault("picture", ""))
            );
        } catch (org.springframework.web.client.RestClientException ex) {
            throw new IllegalArgumentException("Failed to verify Google ID token", ex);
        }
    }

    @Getter
    @AllArgsConstructor
    public static class Payload {
        private final String googleId;
        private final String email;
        private final String name;
        private final String avatar;
    }
} 