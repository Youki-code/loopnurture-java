package org.springframework.samples.loopnurture.users.domain.enums;

import lombok.Getter;

/**
 * 用户角色枚举
 */
@Getter
public enum UserRoleEnum {
    /**
     * 管理员
     */
    ADMIN(1, "管理员"),

    /**
     * 普通成员
     */
    MEMBER(2, "成员"),

    /**
     * 只读用户
     */
    VIEWER(3, "查看者");

    private final Integer code;
    private final String description;

    UserRoleEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public static UserRoleEnum fromCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (UserRoleEnum role : UserRoleEnum.values()) {
            if (role.getCode().equals(code)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Invalid user role code: " + code);
    }
} 