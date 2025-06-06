package org.springframework.samples.loopnurture.users.server.dto;

import lombok.Data;
import org.springframework.samples.loopnurture.users.domain.enums.*;
import java.time.LocalDateTime;

/**
 * 用户响应DTO
 */
@Data
public class UserResponse {
    /**
     * 用户ID
     */
    private String id;

    /**
     * 组织ID
     */
    private String orgId;

    /**
     * 用户唯一标识
     */
    private String userUniq;

    /**
     * 认证类型
     */
    private AuthTypeEnum authType;

    /**
     * OAuth提供商的用户ID
     */
    private String oauthUserId;

    /**
     * 用户类型
     */
    private UserTypeEnum userType;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 用户头像URL
     */
    private String avatarUrl;

    /**
     * 主要邮箱
     */
    private String primaryEmail;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 语言偏好
     */
    private LanguagePreferenceEnum languagePreference;

    /**
     * 用户时区
     */
    private String timezone;

    /**
     * 账户状态
     */
    private AccountStatusEnum accountStatus;

    /**
     * 邮箱是否已验证
     */
    private Boolean emailVerified;

    /**
     * 手机号是否已验证
     */
    private Boolean phoneVerified;

    /**
     * 最后登录时间
     */
    private LocalDateTime lastLoginAt;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
} 