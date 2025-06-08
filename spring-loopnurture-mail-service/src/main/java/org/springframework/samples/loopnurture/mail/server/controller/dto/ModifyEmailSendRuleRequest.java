package org.springframework.samples.loopnurture.mail.server.controller.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 修改邮件发送规则请求DTO
 */
@Data
public class ModifyEmailSendRuleRequest {
    
    /**
     * 规则ID
     */
    @NotBlank(message = "Rule ID cannot be null")
    private String ruleId;

    /**
     * 规则名称（可选，不传入时保持原值）
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
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 最大执行次数
     */
    private Integer maxExecutions;
} 