package org.springframework.samples.loopnurture.mail.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 邮件模板状态枚举
 */
@Getter
@RequiredArgsConstructor
public enum EmailTemplateStatusEnum {
    /**
     * 草稿
     */
    DRAFT(1, "草稿"),

    /**
     * 已禁用
     */
    DISABLED(3, "已禁用"),

    /**
     * 已删除
     */
    DELETED(4, "已删除");

    private final Integer code;
    private final String description;

    /**
     * 根据状态码获取枚举值
     *
     * @param code 状态码
     * @return 枚举值
     */
    public static EmailTemplateStatusEnum fromCode(Integer code) {
        if (code == null) {
            return null;
        }
        
        for (EmailTemplateStatusEnum status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid email template status code: " + code);
    }

    public boolean isDraft() {
        return this == DRAFT;
    }

    public boolean isDisabled() {
        return this == DISABLED;
    }

    public boolean isDeleted() {
        return this == DELETED;
    }
} 