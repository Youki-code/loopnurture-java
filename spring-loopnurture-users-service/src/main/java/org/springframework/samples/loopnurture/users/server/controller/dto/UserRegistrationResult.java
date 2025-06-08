package org.springframework.samples.loopnurture.users.server.controller.dto;

import lombok.Data;
import org.springframework.samples.loopnurture.users.domain.enums.AuthTypeEnum;
import org.springframework.samples.loopnurture.users.domain.model.MarketingUserDO;

/**
 * 用户注册结果对象
 */
@Data
public class UserRegistrationResult {
    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户唯一标识
     */
    private String userUniq;

    /**
     * 组织ID
     */
    private String orgId;

    /**
     * JWT令牌
     */
    private String token;

    public static UserRegistrationResult fromDO(MarketingUserDO user, String token) {
        UserRegistrationResult result = new UserRegistrationResult();
        result.setUserId(user.getSystemUserId());
        result.setUserUniq(user.getUserUniq());
        result.setOrgId(user.getOrgId());
        result.setToken(token);
        return result;
    }
} 