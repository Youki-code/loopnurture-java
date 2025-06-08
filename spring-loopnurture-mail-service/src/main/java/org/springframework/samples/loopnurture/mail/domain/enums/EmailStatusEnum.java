package org.springframework.samples.loopnurture.mail.domain.enums;

import lombok.Getter;

/**
 * 邮件发送状态
 */
@Getter
public enum EmailStatusEnum {
    PENDING(1, "待发送"),
    SENDING(2, "发送中"),
    SENT(3, "已发送"),
    FAILED(4, "发送失败");

    private final int code;
    private final String description;

    EmailStatusEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static EmailStatusEnum fromCode(int code) {
        for (EmailStatusEnum status : values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid email status code: " + code);
    }
} 