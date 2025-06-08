package org.springframework.samples.loopnurture.mail.domain.enums;

import lombok.Getter;

/**
 * 邮件发送规则类型
 */
@Getter
public enum RuleTypeEnum {
    CRON(1, "Cron表达式"),
    FIXED_RATE(2, "固定频率"),
    FIXED_DELAY(3, "固定延迟");

    private final int code;
    private final String description;

    RuleTypeEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static RuleTypeEnum fromCode(int code) {
        for (RuleTypeEnum type : values()) {
            if (type.getCode() == code) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid rule type code: " + code);
    }
} 