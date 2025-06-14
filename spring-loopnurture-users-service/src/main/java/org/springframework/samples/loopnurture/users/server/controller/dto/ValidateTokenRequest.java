package org.springframework.samples.loopnurture.users.server.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 令牌验证请求对象
 */
@Data
public class ValidateTokenRequest {
    @NotBlank
    private String token;
} 