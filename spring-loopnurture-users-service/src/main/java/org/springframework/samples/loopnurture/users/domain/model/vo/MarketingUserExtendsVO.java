package org.springframework.samples.loopnurture.users.domain.model.vo;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 营销用户扩展信息值对象
 */
@Data
public class MarketingUserExtendsVO {
    /**
     * OAuth提供商的用户ID
     */
    private String oauthProviderId;

    /**
     * OAuth访问令牌
     */
    private String oauthAccessToken;

    /**
     * OAuth刷新令牌
     */
    private String oauthRefreshToken;

    /**
     * OAuth令牌过期时间
     */
    private LocalDateTime oauthExpiresAt;
} 