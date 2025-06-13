package org.springframework.samples.loopnurture.mail.domain.enums;

import lombok.Getter;

/**
 * 规则类型枚举
 */
@Getter
public enum RuleTypeEnum implements BaseIntEnum {
    CRON(0, "CRON表达式"),
    FIXED_RATE(1, "固定频率"),
    FIXED_DELAY(2, "固定延迟"),
    IMMEDIATE(3, "立即执行");

    private final Integer code;
    private final String description;

    RuleTypeEnum(Integer code, String description) {
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

    public static RuleTypeEnum fromCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (RuleTypeEnum type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid RuleTypeEnum code: " + code);
    }
} 