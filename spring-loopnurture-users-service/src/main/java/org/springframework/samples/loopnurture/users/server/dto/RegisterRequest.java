package org.springframework.samples.loopnurture.users.server.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.samples.loopnurture.users.domain.enums.AuthTypeEnum;

import javax.validation.constraints.NotNull;

/**
 * 注册请求DTO
 */
@Data
@Schema(description = "注册请求")
public class RegisterRequest {
    /**
     * 认证类型
     */
    @NotNull(message = "认证类型不能为空")
    @Schema(description = "认证类型", required = true, example = "PASSWORD")
    private AuthTypeEnum authType;

    /**
     * 用户登录名（可选，如不提供则使用系统生成的ID）
     */
    @Schema(description = "用户登录名（可选，如不提供则使用系统生成的ID）", example = "testuser")
    private String userUniq;

    /**
     * RSA加密后的密码（密码注册时必填）
     */
    @Schema(description = "RSA加密后的密码（密码注册时必填）", example = "encrypted_password")
    private String encryptedPassword;

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
     * 验证请求参数
     */
    public void validate() {
        if (authType == AuthTypeEnum.PASSWORD) {
            if (encryptedPassword == null || encryptedPassword.trim().isEmpty()) {
                throw new IllegalArgumentException("密码注册时，密码不能为空");
            }
        } else {
            if (oauthUserId == null || oauthUserId.trim().isEmpty()) {
                throw new IllegalArgumentException("OAuth注册时，OAuth用户ID不能为空");
            }
            if (oauthAccessToken == null || oauthAccessToken.trim().isEmpty()) {
                throw new IllegalArgumentException("OAuth注册时，OAuth访问令牌不能为空");
            }
        }
    }
} 