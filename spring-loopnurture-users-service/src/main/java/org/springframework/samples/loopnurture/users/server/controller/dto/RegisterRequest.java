package org.springframework.samples.loopnurture.users.server.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import jakarta.validation.constraints.NotNull;

/**
 * 用户注册请求对象
 */
@Data
@Schema(description = "注册请求")
public class RegisterRequest {
    /**
     * 用户唯一标识（用户名/邮箱/手机号）
     */
    @Schema(description = "用户登录名（可选，如不提供则使用系统生成的ID）", example = "testuser")
    private String userUniq;

    /**
     * 密码（RSA加密后的密文）
     */
    @Schema(description = "RSA加密后的密码（密码注册时必填）", example = "encrypted_password")
    private String password;

    /**
     * 认证类型
     */
    @NotNull
    @Schema(description = "认证类型", required = true, example = "1")
    private Integer authType;

    /**
     * OAuth用户ID（OAuth注册时必填）
     */
    @Schema(description = "OAuth用户ID（OAuth注册时必填）", example = "google_123456")
    private String oauthUserId;

    /**
     * OAuth访问令牌（OAuth注册时必填）
     */
    @Schema(description = "OAuth访问令牌（OAuth注册时必填）", example = "google_token_xxx")
    private String oauthAccessToken;

    /**
     * 用户昵称
     */
    @Schema(description = "用户昵称", example = "John Doe")
    private String nickname;

    /**
     * 头像URL
     */
    @Schema(description = "头像URL", example = "https://example.com/avatar.jpg")
    private String avatarUrl;

    /**
     * 主邮箱
     */
    @Schema(description = "主邮箱")
    private String primaryEmail;

    /**
     * 手机号
     */
    @Schema(description = "手机号")
    private String phone;

    /**
     * 时区
     */
    @Schema(description = "时区")
    private String timezone;

    /**
     * 语言偏好
     */
    @Schema(description = "语言偏好")
    private String languagePreference;

    /**
     * 用户类型
     */
    @Schema(description = "用户类型")
    private Integer userType;

    /**
     * 验证请求参数
     */
    public void validate() {
        if (authType == null) {
            throw new IllegalArgumentException("认证类型不能为空");
        }

        if (authType == 1) { // PASSWORD
            if (StringUtils.isBlank(password)) {
                throw new IllegalArgumentException("密码不能为空");
            }
        } else {
            if (StringUtils.isBlank(oauthUserId)) {
                throw new IllegalArgumentException("OAuth用户ID不能为空");
            }
            if (StringUtils.isBlank(oauthAccessToken)) {
                throw new IllegalArgumentException("OAuth访问令牌不能为空");
            }
        }

        // 至少需要一种联系方式
        if (StringUtils.isBlank(primaryEmail) && StringUtils.isBlank(phone)) {
            throw new IllegalArgumentException("邮箱或手机号至少需要填写一项");
        }
    }
} 