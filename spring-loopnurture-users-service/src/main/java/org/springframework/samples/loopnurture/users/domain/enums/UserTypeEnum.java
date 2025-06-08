package org.springframework.samples.loopnurture.users.domain.enums;

import lombok.Getter;

/**
 * 用户类型枚举
 * 区分用户的业务身份和权限范围
 */
@Getter
public enum UserTypeEnum {
    /**
     * 个人用户
     * 使用免费版或基础版功能
     */
    PERSONAL(1, "普通用户"),

    /**
     * 企业用户
     * 归属于某个企业组织，可以使用企业版功能
     */
    ENTERPRISE(2, "企业用户"),

    /**
     * 系统管理员
     * 可以管理所有组织和用户
     */
    ADMIN(3, "管理员"),

    /**
     * 服务账号
     * 用于系统间集成，API调用等
     */
    SERVICE_ACCOUNT(4, "服务账号"),

    USER("user"),
    GUEST("guest");

    private final Integer code;
    private final String description;

    UserTypeEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    UserTypeEnum(String code) {
        this.code = null;
        this.description = code;
    }

    public static UserTypeEnum fromCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (UserTypeEnum type : UserTypeEnum.values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid UserTypeEnum code: " + code);
    }
} 