package org.springframework.samples.loopnurture.users.domain.enums;

/**
 * 组织状态枚举
 */
public enum OrganizationStatusEnum {
    /**
     * 正常
     */
    ACTIVE(1, "正常"),

    /**
     * 禁用
     */
    DISABLED(2, "禁用"),

    /**
     * 待审核
     */
    PENDING(3, "待审核"),

    /**
     * 已删除
     */
    DELETED(4, "已删除");

    private final Integer code;
    private final String description;

    OrganizationStatusEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static OrganizationStatusEnum fromCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (OrganizationStatusEnum status : OrganizationStatusEnum.values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown organization status code: " + code);
    }
} 