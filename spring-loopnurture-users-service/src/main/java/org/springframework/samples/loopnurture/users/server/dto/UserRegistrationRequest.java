package org.springframework.samples.loopnurture.users.server.dto;

import lombok.Data;
import org.springframework.samples.loopnurture.users.domain.enums.AuthTypeEnum;
import javax.validation.constraints.*;

/**
 * 用户注册请求DTO
 */
@Data
public class UserRegistrationRequest {
    /**
     * 组织ID（可选）
     * 如果不提供，将自动创建默认组织
     */
    private String orgId;

    /**
     * 认证类型
     */
    @NotNull(message = "认证类型不能为空")
    private AuthTypeEnum authType = AuthTypeEnum.PASSWORD;

    /**
     * OAuth提供商的用户ID
     * 当authType为OAuth类型时必填
     * 当userUniq为空时，会用作默认的userUniq
     */
    private String oauthUserId;

    /**
     * OAuth访问令牌
     * 当authType为OAuth类型时必填
     */
    private String oauthAccessToken;

    /**
     * 用户唯一标识（用户名/邮箱）
     * 可选，如果不提供：
     * 1. 对于OAuth用户，默认使用oauthUserId
     * 2. 对于密码认证用户，必须提供且符合格式要求
     */
    @Pattern(regexp = "^$|^[a-zA-Z0-9_-]{4,16}$", message = "用户名必须是4-16位字母、数字、下划线或连字符")
    private String userUniq;

    /**
     * 用户昵称
     * 可选，如果不提供：
     * 1. 对于OAuth用户，默认使用oauthUserId
     * 2. 对于密码认证用户，默认使用userUniq
     */
    @Size(min = 2, max = 50, message = "昵称长度必须在2-50个字符之间")
    private String nickname;

    /**
     * 主要邮箱（可选）
     */
    @Email(message = "邮箱格式不正确")
    private String primaryEmail;

    /**
     * 头像URL
     * OAuth注册时可以直接使用OAuth提供商的头像
     */
    private String avatarUrl;

    /**
     * 手机号码（可选）
     */
    @Pattern(regexp = "^$|^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    /**
     * 语言偏好（可选）
     * 默认值由系统配置决定
     */
    @Pattern(regexp = "^$|^(ZH_CN|EN_US|JA_JP|KO_KR)$", message = "不支持的语言类型")
    private String languagePreference;

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