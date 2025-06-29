package org.springframework.samples.loopnurture.mail.server.controller.dto;

import lombok.Data;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * 创建邮件发送规则请求DTO
 */
@Data
public class CreateEmailSendRuleRequest {

    /**
     * 规则名称（可选，不传入时自动生成）
     */
    @Size(max = 100, message = "Rule name length cannot exceed 100")
    private String ruleName;


    /**
     * 模板ID
     */
    @NotNull(message = "Template ID cannot be null")
    @Size(max = 50, message = "Template ID length cannot exceed 50")
    private String templateId;

    /**
     * 规则类型：1-立即发送，2-定时发送，3-周期发送
     */
    @NotNull(message = "Rule type cannot be null")
    private Integer ruleType;

    /**
     * Cron表达式（定时发送时必填）
     */
    @Size(max = 100, message = "Cron expression length cannot exceed 100")
    private String cronExpression;

    /**
     * 固定频率，单位：毫秒（周期发送时必填）
     */
    private Long fixedRate;

    /**
     * 固定延迟，单位：毫秒（周期发送时必填）
     */
    private Long fixedDelay;

    /**
     * 收件人列表
     */
    private List<String> recipients;

    /**
     * 抄送人列表
     */
    private List<String> cc;

    /**
     * 密送人列表
     */
    private List<String> bcc;

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
     * 规则描述
     */
    @Size(max = 255, message = "Description length cannot exceed 255")
    private String description;

    /**
     * 邮件主题
     */
    @Size(max = 200, message = "Subject length cannot exceed 200")
    private String subject;

    /**
     * 发件人邮箱（可选），为空时系统将使用默认 support@eraiser.pro
     */
    @Size(max = 100, message = "Sender email length cannot exceed 100")
    private String fromEmail;
} 