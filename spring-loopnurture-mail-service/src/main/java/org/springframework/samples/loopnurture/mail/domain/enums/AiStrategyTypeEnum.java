package org.springframework.samples.loopnurture.mail.domain.enums;

import lombok.Getter;

/**
 * AI 策略类型枚举
 */
@Getter
public enum AiStrategyTypeEnum implements BaseIntEnum {
    UNKNOWN(-1, "未知"),
    MARKETING_MAIL(0, "营销邮件");

    private final Integer code;
    private final String description;

    AiStrategyTypeEnum(Integer code, String description) {
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

    public static AiStrategyTypeEnum fromCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (AiStrategyTypeEnum value : values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        throw new IllegalArgumentException("Invalid AiStrategyTypeEnum code: " + code);
    }
} 