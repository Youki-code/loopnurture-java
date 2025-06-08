package org.springframework.samples.loopnurture.users.domain.model.vo;

import lombok.Data;

/**
 * 营销用户扩展信息值对象
 */
@Data
public class MarketingUserExtendsVO {
    /**
     * OAuth访问令牌
     */
    private String oauthAccessToken;

    /**
     * OAuth刷新令牌
     */
    private String oauthRefreshToken;

    /**
     * OAuth范围
     */
    private String oauthScope;

    /**
     * OAuth令牌类型
     */
    private String oauthTokenType;

    /**
     * OAuth令牌过期时间（秒）
     */
    private Long oauthExpiresIn;

    /**
     * OAuth ID令牌
     */
    private String oauthIdToken;
} 