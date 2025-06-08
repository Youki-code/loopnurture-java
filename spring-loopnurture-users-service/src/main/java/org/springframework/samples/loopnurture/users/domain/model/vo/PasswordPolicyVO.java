package org.springframework.samples.loopnurture.users.domain.model.vo;

import lombok.Data;

/**
 * 密码策略值对象
 * 定义组织的密码要求和规则
 */
@Data
public class PasswordPolicyVO {
    /**
     * 最小密码长度
     */
    private Integer minLength = 8;

    /**
     * 最大密码长度
     */
    private Integer maxLength = 32;

    /**
     * 是否要求包含大写字母
     */
    private Boolean requireUpperCase = true;

    /**
     * 是否要求包含小写字母
     */
    private Boolean requireLowerCase = true;

    /**
     * 是否要求包含数字
     */
    private Boolean requireNumbers = true;

    /**
     * 是否要求包含特殊字符
     */
    private Boolean requireSpecialChars = true;

    /**
     * 密码过期天数
     */
    private Integer expirationDays = 90;

    /**
     * 密码历史记录数
     */
    private Integer historyCount = 5;
} 