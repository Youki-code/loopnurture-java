package org.springframework.samples.loopnurture.users.domain.enums;

/**
 * 账户状态枚举
 */
public enum AccountStatusEnum {
    /**
     * 正常
     */
    ACTIVE(1, "正常"),

    /**
     * 已锁定
     */
    LOCKED(2, "已锁定"),

    /**
     * 已禁用
     */
    DISABLED(3, "已禁用"),

    /**
     * 已删除
     */
    DELETED(4, "已删除"),

    /**
     * 待验证
     */
    PENDING_VERIFICATION(5, "待验证");

    private final Integer code;
    private final String description;

    AccountStatusEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static AccountStatusEnum fromCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (AccountStatusEnum status : AccountStatusEnum.values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown account status code: " + code);
    }
} 