package org.springframework.samples.loopnurture.mail.server.feign.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 请求 Users-Service /api/v1/auth/validate
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidateTokenRequest {
    private String token;
} 