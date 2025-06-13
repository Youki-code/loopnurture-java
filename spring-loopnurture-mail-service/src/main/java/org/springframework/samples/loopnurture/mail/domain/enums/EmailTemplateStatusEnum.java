package org.springframework.samples.loopnurture.mail.domain.enums;

/**
 * 邮件模板状态枚举
 */
public enum EmailTemplateStatusEnum implements BaseIntEnum {
    DRAFT(1, "草稿"),
    PUBLISHED(2, "已发布"),
    ARCHIVED(3, "已归档");

    private final Integer code;
    private final String description;

    EmailTemplateStatusEnum(Integer code, String description) {
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

    public static EmailTemplateStatusEnum fromCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (EmailTemplateStatusEnum status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
} 