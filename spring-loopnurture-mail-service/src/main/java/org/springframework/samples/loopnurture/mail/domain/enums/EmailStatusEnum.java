package org.springframework.samples.loopnurture.mail.domain.enums;

import lombok.Getter;

/**
 * 邮件状态枚举
 */
@Getter
public enum EmailStatusEnum implements BaseIntEnum {
    PENDING(0, "待发送"),
    SENDING(1, "发送中"),
    SENT(2, "已发送"),
    FAILED(3, "发送失败");

    private final Integer code;
    private final String description;

    EmailStatusEnum(Integer code, String description) {
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

    public static EmailStatusEnum fromCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (EmailStatusEnum status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown email status code: " + code);
    }

    public static EmailStatusEnum fromValue(Integer code) {
        return fromCode(code);
    }

    public boolean isPending() {
        return this == PENDING;
    }

    public boolean isSending() {
        return this == SENDING;
    }

    public boolean isSent() {
        return this == SENT;
    }

    public boolean isFailed() {
        return this == FAILED;
    }
} 