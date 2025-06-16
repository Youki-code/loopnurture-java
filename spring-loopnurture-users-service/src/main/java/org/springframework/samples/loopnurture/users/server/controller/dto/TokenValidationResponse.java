package org.springframework.samples.loopnurture.users.server.controller.dto;

import lombok.Data;

/**
 * 令牌验证响应对象
 */
@Data
public class TokenValidationResponse {
    /**
     * 令牌是否有效
     */
    private boolean valid;

    /**
     * 错误信息（令牌无效时返回）
     */
    private String errorMessage;

    /**
     * 用户ID（令牌有效时返回）
     */
    private Long userId;

    /**
     * 用户唯一标识（令牌有效时返回）
     */
    private String userUniq;

    /**
     * 组织编码（令牌有效时返回，单组织场景下使用）
     */
    private String orgCode;
} 