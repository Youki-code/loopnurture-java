package org.springframework.samples.loopnurture.mail.server.feign.dto;

import lombok.Data;

/**
 * 用户信息DTO
 */
@Data
public class UserInfo {
    /**
     * 验证结果
     */
    private boolean valid;

    /**
     * 错误信息
     */
    private String errorMessage;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户唯一标识
     */
    private String userUniq;

    /**
     * 组织ID
     */
    private String orgId;
} 