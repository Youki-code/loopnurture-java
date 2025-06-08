package org.springframework.samples.loopnurture.users.domain.enums;

/**
 * 用户认证类型枚举
 */
public enum AuthTypeEnum {
    /**
     * 本地密码认证
     */
    PASSWORD(1, "密码认证"),

    /**
     * Google OAuth认证
     */
    GOOGLE_OAUTH(2, "Google认证"),

    /**
     * GitHub OAuth认证
     */
    GITHUB_OAUTH(3, "GitHub认证"),

    /**
     * Facebook OAuth认证
     */
    FACEBOOK_OAUTH(4, "Facebook认证"),

    /**
     * Twitter OAuth认证
     */
    TWITTER_OAUTH(5, "Twitter认证"),

    /**
     * LinkedIn OAuth认证
     */
    LINKEDIN_OAUTH(6, "LinkedIn认证"),

    /**
     * Microsoft OAuth认证
     */
    MICROSOFT_OAUTH(7, "Microsoft认证"),

    /**
     * Apple OAuth认证
     */
    APPLE_OAUTH(8, "Apple认证");

    private final Integer code;
    private final String description;

    AuthTypeEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static AuthTypeEnum fromCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (AuthTypeEnum type : AuthTypeEnum.values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown auth type code: " + code);
    }
} 