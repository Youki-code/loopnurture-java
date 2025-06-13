package org.springframework.samples.loopnurture.mail.domain.enums;

/**
 * 收件人类型枚举
 */
public enum RecipientTypeEnum implements BaseIntEnum {
    TO(1, "收件人"),
    CC(2, "抄送"),
    BCC(3, "密送");

    private final Integer code;
    private final String description;

    RecipientTypeEnum(Integer code, String description) {
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

    public static RecipientTypeEnum fromCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (RecipientTypeEnum type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
} 