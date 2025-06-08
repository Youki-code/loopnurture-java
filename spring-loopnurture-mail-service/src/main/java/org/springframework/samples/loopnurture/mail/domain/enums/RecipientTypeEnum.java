package org.springframework.samples.loopnurture.mail.domain.enums;

import lombok.Getter;

/**
 * 收件人类型
 */
@Getter
public enum RecipientTypeEnum {
    FIXED(1, "固定收件人"),
    DYNAMIC(2, "动态查询");

    private final int code;
    private final String description;

    RecipientTypeEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static RecipientTypeEnum fromCode(int code) {
        for (RecipientTypeEnum type : values()) {
            if (type.getCode() == code) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid recipient type code: " + code);
    }
} 