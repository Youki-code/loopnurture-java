package org.springframework.samples.loopnurture.mail.server.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.Date;

/**
 * 更新邮件发送规则请求DTO
 */
@Data
public class UpdateEmailSendRuleRequest {
    /**
     * 规则ID
     */
    @NotBlank(message = "规则ID不能为空")
    private String ruleId;

    /**
     * 规则名称
     */
    @NotBlank(message = "规则名称不能为空")
    private String ruleName;

    /**
     * 模板ID
     */
    @NotBlank(message = "模板ID不能为空")
    private String templateId;

    /**
     * 规则类型：1-定时，2-固定频率，3-固定延迟
     */
    @NotNull(message = "规则类型不能为空")
    private Integer ruleType;

    /**
     * Cron表达式（定时规则）
     */
    private String cronExpression;

    /**
     * 固定频率（毫秒）
     */
    private Long fixedRate;

    /**
     * 固定延迟（毫秒）
     */
    private Long fixedDelay;

    /**
     * 收件人列表
     */
    private java.util.List<String> recipients;

    /**
     * 抄送列表
     */
    private java.util.List<String> cc;

    /**
     * 密送列表
     */
    private java.util.List<String> bcc;

    /**
     * 启用状态：1-启用，0-禁用
     */
    private Integer enableStatus;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 最大执行次数
     */
    private Integer maxExecutions;

    /**
     * 邮件主题
     */
    private String subject;

    /**
     * 发件人邮箱（可选）
     */
    private String fromEmail;
} 