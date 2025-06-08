package org.springframework.samples.loopnurture.users.domain.model.vo;

import lombok.Data;

/**
 * 组织设置值对象
 * 包含组织的各种配置参数，如用户限制、模板限制等
 */
@Data
public class OrganizationSettingsVO {
    /**
     * 最大用户数
     */
    private Integer maxUsers;

    /**
     * 最大模板数
     */
    private Integer maxTemplates;

    /**
     * 最大规则数
     */
    private Integer maxRules;

    /**
     * 密码策略配置
     */
    private PasswordPolicyVO passwordPolicy;

    /**
     * 通知设置
     */
    private NotificationSettingsVO notificationSettings;

    /**
     * 是否允许成员自行注册
     * 如果为true，用户可以自行申请加入组织
     * 如果为false，只能由管理员手动添加成员
     */
    private Boolean allowMemberRegistration = false;

    /**
     * 是否需要管理员审批新成员
     * 当allowMemberRegistration为true时生效
     * 如果为true，新成员需要等待管理员审批
     * 如果为false，新成员可以直接加入
     */
    private Boolean requireAdminApproval = true;

    /**
     * 是否允许社交账号登录
     * 如果为true，成员可以使用第三方社交账号（如Google、GitHub）登录
     * 如果为false，只能使用用户名密码登录
     */
    private Boolean allowSocialLogin = true;

    /**
     * 允许的社交登录提供商列表
     * 当allowSocialLogin为true时生效
     * 可选值：GOOGLE, GITHUB, MICROSOFT等
     */
    private String[] allowedSocialProviders = {"GOOGLE", "GITHUB"};

    /**
     * 默认用户角色
     * 新成员加入组织时的默认角色
     * 可选值：VIEWER（查看者）, EDITOR（编辑者）, ADMIN（管理员）
     */
    private String defaultUserRole = "VIEWER";

    /**
     * 配额限制
     * 包含组织的各种资源使用限制
     */
    private QuotaSettingsVO quotaSettings = new QuotaSettingsVO();

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
         * 默认值：8
         */
        private Integer minLength = 8;

        /**
         * 最大密码长度
         * 默认值：32
         */
        private Integer maxLength = 32;

        /**
         * 是否要求包含大写字母
         * 默认值：true
         */
        private Boolean requireUpperCase = true;

        /**
         * 是否要求包含小写字母
         * 默认值：true
         */
        private Boolean requireLowerCase = true;

        /**
         * 是否要求包含数字
         * 默认值：true
         */
        private Boolean requireNumbers = true;

        /**
         * 是否要求包含特殊字符
         * 默认值：true
         */
        private Boolean requireSpecialChars = true;

        /**
         * 密码过期天数
         * 0表示永不过期
         * 默认值：90天
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
         * 默认值：true
         */
        private Boolean enableEmailNotifications = true;

        /**
         * 是否启用系统通知
         * 默认值：true
         */
        private Boolean enableSystemNotifications = true;

        /**
         * 是否发送每周报告
         * 默认值：false
         */
        private Boolean sendWeeklyReport = false;

        /**
         * 是否通知用户角色变更
         * 默认值：true
         */
        private Boolean notifyRoleChanges = true;

        /**
         * 是否通知新成员加入
         * 默认值：true
         */
        private Boolean notifyNewMembers = true;
    }
} 