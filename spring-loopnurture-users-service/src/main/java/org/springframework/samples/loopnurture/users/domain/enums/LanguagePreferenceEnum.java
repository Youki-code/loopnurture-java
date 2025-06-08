package org.springframework.samples.loopnurture.users.domain.enums;

import lombok.Getter;

/**
 * 语言偏好枚举
 */
@Getter
public enum LanguagePreferenceEnum {
    EN_US("en-US"),
    ZH_CN("zh-CN"),
    ZH_TW("zh-TW"),
    JA_JP("ja-JP"),
    KO_KR("ko-KR");

    private final String code;

    LanguagePreferenceEnum(String code) {
        this.code = code;
    }

    public static LanguagePreferenceEnum fromCode(String code) {
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