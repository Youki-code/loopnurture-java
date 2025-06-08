package org.springframework.samples.loopnurture.mail.server.feign.dto;

import lombok.Data;

/**
 * Token验证请求DTO
 */
@Data
public class TokenValidationRequest {
    private String token;

    public static TokenValidationRequest of(String token) {
        TokenValidationRequest request = new TokenValidationRequest();
        request.setToken(token);
        return request;
    }
} 