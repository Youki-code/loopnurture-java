package org.springframework.samples.loopnurture.mail.domain.enums;

import lombok.Getter;

/**
 * 启用状态枚举
 */
@Getter
public enum EnableStatusEnum implements BaseIntEnum {
    ENABLED(1, "启用"),
    DISABLED(0, "禁用");

    private final Integer code;
    private final String description;

    EnableStatusEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public static EnableStatusEnum fromCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (EnableStatusEnum status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid EnableStatusEnum code: " + code);
    }

    public boolean isEnabled() {
        return this == ENABLED;
    }

    public boolean isDisabled() {
        return this == DISABLED;
    }
} 