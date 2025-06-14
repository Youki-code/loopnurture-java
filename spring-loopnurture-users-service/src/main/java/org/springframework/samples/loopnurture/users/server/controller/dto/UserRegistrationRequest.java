package org.springframework.samples.loopnurture.users.server.controller.dto;

import org.springframework.samples.loopnurture.users.domain.enums.AuthTypeEnum;
import org.springframework.samples.loopnurture.users.domain.enums.UserTypeEnum;

/**
 * 已废弃，使用 RegisterRequest。
 */
@Deprecated
public class UserRegistrationRequest extends RegisterRequest {

    // 兼容测试代码中的加密密码字段
    public void setEncryptedPassword(String encryptedPassword) {
        super.setPassword(encryptedPassword);
    }

    // 兼容测试使用枚举设置方法
    public void setAuthType(AuthTypeEnum authType) {
        super.setAuthType(authType != null ? authType.getCode() : null);
    }

    public void setUserType(UserTypeEnum userType) {
        super.setUserType(userType != null ? userType.getCode() : null);
    }
} 