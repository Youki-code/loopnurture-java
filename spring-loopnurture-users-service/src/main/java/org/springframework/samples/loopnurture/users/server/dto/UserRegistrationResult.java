package org.springframework.samples.loopnurture.users.server.dto;

import lombok.Data;

/**
 * 用户注册结果DTO
 */
@Data
public class UserRegistrationResult {
    /**
     * 用户信息
     */
    private UserResponse user;

    /**
     * 组织信息
     * 当注册时创建了新组织时返回
     */
    private OrganizationResponse organization;

    /**
     * 是否创建了新组织
     */
    private boolean newOrganizationCreated;
} 