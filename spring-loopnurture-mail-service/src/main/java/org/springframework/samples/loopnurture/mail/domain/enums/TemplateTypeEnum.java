package org.springframework.samples.loopnurture.mail.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 模板类型枚举
 */
@Getter
@RequiredArgsConstructor
public enum TemplateTypeEnum {
    
    /**
     * 系统模板
     */
    SYSTEM(1, "系统模板"),
    
    /**
     * 自定义模板
     */
    CUSTOM(2, "自定义模板");
    
    private final Integer code;
    private final String desc;
    
    public static TemplateTypeEnum fromCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (TemplateTypeEnum type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown template type code: " + code);
    }
} 