package org.springframework.samples.loopnurture.users.server.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.samples.loopnurture.users.domain.enums.AuthTypeEnum;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 登录请求DTO
 */
@Data
@Schema(description = "登录请求")
public class LoginRequest {
    /**
     * 认证类型
     */
    @NotNull(message = "认证类型不能为空")
    @Schema(description = "认证类型", required = true, example = "PASSWORD")
    private AuthTypeEnum authType;

    /**
     * 用户登录名（密码登录时必填）
     */
    @Schema(description = "用户登录名（密码登录时必填）", example = "testuser")
    private String userUniq;

    /**
     * 登录密码（密码登录时必填）
     */
    @Schema(description = "登录密码（密码登录时必填）", example = "123456")
    private String password;

    /**
     * OAuth用户ID（OAuth登录时必填）
     */
    @Schema(description = "OAuth用户ID（OAuth登录时必填）", example = "google_123456")
    private String oauthUserId;

    /**
     * OAuth访问令牌（OAuth登录时必填）
     */
    @Schema(description = "OAuth访问令牌（OAuth登录时必填）", example = "google_token_xxx")
    private String oauthAccessToken;

    /**
     * 验证请求参数
     */
    public void validate() {
        if (authType == AuthTypeEnum.PASSWORD) {
            if (userUniq == null || userUniq.trim().isEmpty()) {
                throw new IllegalArgumentException("密码登录时，登录名不能为空");
            }
            if (password == null || password.trim().isEmpty()) {
                throw new IllegalArgumentException("密码登录时，密码不能为空");
            }
        } else {
            if (oauthUserId == null || oauthUserId.trim().isEmpty()) {
                throw new IllegalArgumentException("OAuth登录时，OAuth用户ID不能为空");
            }
            if (oauthAccessToken == null || oauthAccessToken.trim().isEmpty()) {
                throw new IllegalArgumentException("OAuth登录时，OAuth访问令牌不能为空");
            }
        }
    }
} 