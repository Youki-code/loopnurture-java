package org.springframework.samples.loopnurture.users.domain.enums;

/**
 * 用户认证类型枚举
 */
public enum AuthTypeEnum {
    /**
     * 本地密码认证
     */
    PASSWORD,

    /**
     * Google OAuth认证
     */
    GOOGLE_OAUTH,

    /**
     * GitHub OAuth认证
     */
    GITHUB_OAUTH,

    /**
     * 企业SSO认证
     */
    ENTERPRISE_SSO
} 