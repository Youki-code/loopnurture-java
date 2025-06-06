package org.springframework.samples.loopnurture.users.domain.enums;

/**
 * 语言偏好枚举
 */
public enum LanguagePreferenceEnum {
    ZH_CN(1, "简体中文"),
    EN_US(2, "美式英语"),
    JA_JP(3, "日语"),
    KO_KR(4, "韩语");

    private final Integer code;
    private final String description;

    LanguagePreferenceEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static LanguagePreferenceEnum fromCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (LanguagePreferenceEnum lang : LanguagePreferenceEnum.values()) {
            if (lang.getCode().equals(code)) {
                return lang;
            }
        }
        throw new IllegalArgumentException("Unknown language preference code: " + code);
    }
} 