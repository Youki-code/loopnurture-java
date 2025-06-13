package org.springframework.samples.loopnurture.mail.domain.enums;

import lombok.Getter;

/**
 * 内容类型枚举
 */
@Getter
public enum ContentTypeEnum implements BaseIntEnum {
    TEXT(0, "纯文本"),
    HTML(1, "HTML");

    private final Integer code;
    private final String description;

    ContentTypeEnum(Integer code, String description) {
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

    public static ContentTypeEnum fromCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (ContentTypeEnum type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid ContentTypeEnum code: " + code);
    }
} 