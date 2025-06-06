package org.springframework.samples.loopnurture.users.domain.model.vo;

import lombok.Data;

/**
 * 组织设置值对象
 */
@Data
public class OrganizationSettingsVO {
    /**
     * 是否允许成员自行注册
     */
    private Boolean allowMemberRegistration = false;

    /**
     * 是否需要管理员审批新成员
     */
    private Boolean requireAdminApproval = true;

    /**
     * 是否允许社交账号登录
     */
    private Boolean allowSocialLogin = true;

    /**
     * 允许的社交登录提供商列表
     */
    private String[] allowedSocialProviders = {"GOOGLE", "GITHUB"};

    /**
     * 默认用户角色
     */
    private String defaultUserRole = "VIEWER";

    /**
     * 配额限制
     */
    private QuotaSettingsVO quotaSettings = new QuotaSettingsVO();

    /**
     * 密码策略
     */
    private PasswordPolicyVO passwordPolicy = new PasswordPolicyVO();

    /**
     * 通知设置
     */
    private NotificationSettingsVO notificationSettings = new NotificationSettingsVO();

    /**
     * 配额限制值对象
     */
    @Data
    public static class QuotaSettingsVO {
        /**
         * 组织最大用户数限制
         * 默认值：5
         */
        private Integer maxUsers = 5;

        /**
         * 组织最大模板数限制
         * 默认值：100
         */
        private Integer maxTemplates = 100;

        /**
         * 组织最大规则数限制
         * 默认值：50
         */
        private Integer maxRules = 50;
    }

    /**
     * 密码策略值对象
     */
    @Data
    public static class PasswordPolicyVO {
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
         * 密码过期天数，0表示永不过期
         */
        private Integer passwordExpiryDays = 90;
    }

    /**
     * 通知设置值对象
     */
    @Data
    public static class NotificationSettingsVO {
        /**
         * 是否启用邮件通知
         */
        private Boolean enableEmailNotifications = true;

        /**
         * 是否启用系统通知
         */
        private Boolean enableSystemNotifications = true;

        /**
         * 是否发送每周报告
         */
        private Boolean sendWeeklyReport = false;

        /**
         * 是否通知用户角色变更
         */
        private Boolean notifyRoleChanges = true;

        /**
         * 是否通知新成员加入
         */
        private Boolean notifyNewMembers = true;
    }
} 