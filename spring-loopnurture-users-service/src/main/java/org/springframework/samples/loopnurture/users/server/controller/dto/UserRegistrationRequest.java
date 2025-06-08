package org.springframework.samples.loopnurture.users.server.controller.dto;

import lombok.Data;
import org.springframework.samples.loopnurture.users.domain.enums.AuthTypeEnum;
import org.springframework.samples.loopnurture.users.domain.enums.LanguagePreferenceEnum;
import org.springframework.samples.loopnurture.users.domain.enums.UserTypeEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 用户注册请求对象（包含组织信息）
 */
@Data
public class UserRegistrationRequest {
    /**
     * 用户基本信息
     */
    private RegisterRequest userInfo;

    /**
     * 组织ID（可选，如果要加入现有组织）
     */
    private String orgId;

    /**
     * 组织名称（可选，如果要创建新组织）
     */
    private String orgName;

    /**
     * 组织描述（可选，如果要创建新组织）
     */
    private String orgDescription;

    /**
     * 用户唯一标识（可选）
     */
    private String userUniq;

    /**
     * RSA加密后的密码
     */
    private String encryptedPassword;

    /**
     * OAuth用户ID
     */
    private String oauthUserId;

    /**
     * OAuth访问令牌
     */
    private String oauthAccessToken;

    /**
     * 认证类型
     */
    @NotNull(message = "认证类型不能为空")
    private AuthTypeEnum authType;

    /**
     * 用户类型
     */
    @NotNull(message = "用户类型不能为空")
    private UserTypeEnum userType;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 主邮箱
     */
    @NotBlank(message = "主邮箱不能为空")
    private String primaryEmail;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 头像URL
     * OAuth注册时可以直接使用OAuth提供商的头像
     */
    private String avatarUrl;

    /**
     * 语言偏好（可选）
     * 默认值由系统配置决定
     */
    private LanguagePreferenceEnum languagePreference;

    /**
     * 时区（可选）
     * 默认值由系统配置决定
     */
    private String timezone;

    /**
     * 获取实际的用户唯一标识
     * 如果userUniq为空，则返回oauthUserId
     */
    public String getEffectiveUserUniq() {
        return userUniq != null && !userUniq.isEmpty() ? userUniq : oauthUserId;
    }

    /**
     * 获取实际的用户昵称
     * 如果nickname为空，则返回oauthUserId或userUniq
     */
    public String getEffectiveNickname() {
        if (nickname != null && !nickname.isEmpty()) {
            return nickname;
        }
        return getEffectiveUserUniq();
    }

    /**
     * 验证请求的有效性
     * @throws IllegalArgumentException 如果请求无效
     */
    public void validate() {
        if (authType == AuthTypeEnum.PASSWORD) {
            if (userUniq == null || userUniq.isEmpty()) {
                throw new IllegalArgumentException("密码认证方式下用户名不能为空");
            }
        } else {
            // OAuth类型认证
            if (oauthUserId == null || oauthUserId.isEmpty()) {
                throw new IllegalArgumentException("OAuth认证方式下oauthUserId不能为空");
            }
            if (oauthAccessToken == null || oauthAccessToken.isEmpty()) {
                throw new IllegalArgumentException("OAuth认证方式下oauthAccessToken不能为空");
            }
        }
    }
} 