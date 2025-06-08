package org.springframework.samples.loopnurture.users.server.controller.dto;

import lombok.Data;
import org.springframework.samples.loopnurture.users.domain.enums.*;
import java.time.LocalDateTime;
import org.springframework.samples.loopnurture.users.domain.model.MarketingUserDO;

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
     * 系统用户ID
     */
    private Long systemUserId;

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

    public static UserResponse fromDO(MarketingUserDO user) {
        if (user == null) {
            return null;
        }

        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setSystemUserId(user.getSystemUserId());
        response.setOrgId(user.getOrgId());
        response.setUserUniq(user.getUserUniq());
        response.setAuthType(user.getAuthType());
        response.setUserType(user.getUserType());
        response.setNickname(user.getNickname());
        response.setAvatarUrl(user.getAvatarUrl());
        response.setPrimaryEmail(user.getPrimaryEmail());
        response.setPhone(user.getPhone());
        response.setLanguagePreference(user.getLanguagePreference());
        response.setTimezone(user.getTimezone());
        response.setAccountStatus(user.getAccountStatus());
        response.setEmailVerified(user.getEmailVerified());
        response.setPhoneVerified(user.getPhoneVerified());
        response.setLastLoginAt(user.getLastLoginAt());
        response.setCreatedAt(user.getCreatedAt());
        return response;
    }
} 