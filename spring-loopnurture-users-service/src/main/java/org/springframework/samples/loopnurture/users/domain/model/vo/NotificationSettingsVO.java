package org.springframework.samples.loopnurture.users.domain.model.vo;

import lombok.Data;

/**
 * 通知设置值对象
 * 定义组织的通知配置
 */
@Data
public class NotificationSettingsVO {
    /**
     * 是否启用邮件通知
     */
    private Boolean enableEmailNotifications = true;

    /**
     * 是否启用系统通知
     */
    private Boolean enableSystemNotifications = true;

    /**
     * 是否启用重要事件通知
     */
    private Boolean enableImportantEvents = true;

    /**
     * 是否启用营销通知
     */
    private Boolean enableMarketingNotifications = false;

    /**
     * 每日通知限制
     */
    private Integer dailyNotificationLimit = 10;

    /**
     * 通知时间窗口开始（24小时制）
     */
    private Integer notificationWindowStart = 9;

    /**
     * 通知时间窗口结束（24小时制）
     */
    private Integer notificationWindowEnd = 18;
} 