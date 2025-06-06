package org.springframework.samples.loopnurture.users.domain.enums;

/**
 * 账户状态枚举
 */
public enum AccountStatusEnum {
    ACTIVE(1, "活跃"),
    INACTIVE(2, "不活跃"),
    SUSPENDED(3, "已停用");

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