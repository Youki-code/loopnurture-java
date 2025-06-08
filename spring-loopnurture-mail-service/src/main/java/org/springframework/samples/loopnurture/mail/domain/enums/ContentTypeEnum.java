package org.springframework.samples.loopnurture.mail.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 内容类型枚举
 */
@Getter
@RequiredArgsConstructor
public enum ContentTypeEnum {
    
    /**
     * 纯文本
     */
    TEXT(1, "纯文本"),
    
    /**
     * HTML格式
     */
    HTML(2, "HTML格式");
    
    private final Integer code;
    private final String desc;
    
    public static ContentTypeEnum fromCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (ContentTypeEnum type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown content type code: " + code);
    }
} 