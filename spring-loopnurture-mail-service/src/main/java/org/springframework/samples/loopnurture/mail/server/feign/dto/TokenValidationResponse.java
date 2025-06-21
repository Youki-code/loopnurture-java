package org.springframework.samples.loopnurture.mail.server.feign.dto;

import lombok.Data;

/**
 * 响应 Users-Service /api/v1/auth/validate
 */
@Data
public class TokenValidationResponse {
    private boolean valid;
    private String userId;
    private String orgCode;
} 