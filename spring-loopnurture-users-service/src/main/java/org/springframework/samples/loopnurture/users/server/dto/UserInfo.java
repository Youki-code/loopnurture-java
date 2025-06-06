package org.springframework.samples.loopnurture.users.server.dto;

import lombok.Data;

/**
 * 用户信息DTO
 */
@Data
public class UserInfo {
    /**
     * 用户ID
     */
    private String userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 组织ID
     */
    private String orgId;

    /**
     * 组织名称
     */
    private String orgName;
} 