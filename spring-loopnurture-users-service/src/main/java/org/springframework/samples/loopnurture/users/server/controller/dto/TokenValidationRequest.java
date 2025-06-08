package org.springframework.samples.loopnurture.users.server.controller.dto;

import lombok.Data;

/**
 * 令牌验证请求对象
 */
@Data
public class TokenValidationRequest {
    /**
     * JWT令牌
     */
    private String token;
} 