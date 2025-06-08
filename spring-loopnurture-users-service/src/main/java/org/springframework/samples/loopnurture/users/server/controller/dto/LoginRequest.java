package org.springframework.samples.loopnurture.users.server.controller.dto;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import jakarta.validation.constraints.NotNull;

/**
 * 登录请求对象
 */
@Data
public class LoginRequest {
    @NotNull
    private Integer authType;

    // 用于密码登录
    private String userUniq;
    private String password;

    // 用于OAuth登录
    private String oauthUserId;
    private String oauthAccessToken;

    private String email;
    private String phone;

    /**
     * 验证请求参数
     */
    public void validate() {
        if (authType == null) {
            throw new IllegalArgumentException("认证类型不能为空");
        }

        if (authType == 1) { // PASSWORD
            if (StringUtils.isBlank(userUniq)) {
                throw new IllegalArgumentException("用户名不能为空");
            }
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
    }

    public boolean isEmailLogin() {
        return StringUtils.isNotBlank(email) && StringUtils.isBlank(userUniq) && StringUtils.isNotBlank(password);
    }

    public boolean isPhoneLogin() {
        return StringUtils.isNotBlank(phone) && StringUtils.isBlank(userUniq) && StringUtils.isNotBlank(password);
    }

    public boolean isOAuthLogin() {
        return StringUtils.isNotBlank(oauthUserId) && authType != 1; // Not PASSWORD
    }
} 