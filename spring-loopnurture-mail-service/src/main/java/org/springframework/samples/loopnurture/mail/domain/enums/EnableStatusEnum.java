package org.springframework.samples.loopnurture.mail.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 启用状态枚举
 */
@Getter
@RequiredArgsConstructor
public enum EnableStatusEnum {
    /**
     * 启用
     */
    ENABLED(1, "启用"),

    /**
     * 禁用
     */
    DISABLED(0, "禁用");

    private final Integer code;
    private final String description;

    /**
     * 根据状态码获取枚举值
     *
     * @param code 状态码
     * @return 枚举值
     */
    public static EnableStatusEnum fromCode(Integer code) {
        if (code == null) {
            return null;
        }
        
        for (EnableStatusEnum status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid enable status code: " + code);
    }

    public boolean isEnabled() {
        return this == ENABLED;
    }

    public boolean isDisabled() {
        return this == DISABLED;
    }
} 