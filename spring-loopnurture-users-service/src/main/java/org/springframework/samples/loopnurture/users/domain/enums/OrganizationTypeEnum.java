package org.springframework.samples.loopnurture.users.domain.enums;

/**
 * 组织类型枚举
 */
public enum OrganizationTypeEnum {
    ENTERPRISE(1, "企业"),
    TEAM(2, "团队"),
    PERSONAL(3, "个人");

    private final Integer code;
    private final String description;

    OrganizationTypeEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static OrganizationTypeEnum fromCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (OrganizationTypeEnum type : OrganizationTypeEnum.values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown organization type code: " + code);
    }
} 