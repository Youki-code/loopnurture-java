package org.springframework.samples.loopnurture.mail.server.controller.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.Date;
import java.util.List;

/**
 * 邮件发送规则详情响应DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailSendRuleDetailResponse {
    /**
     * 规则ID
     */
    private String ruleId;

    /**
     * 规则名称
     */
    private String ruleName;

    /**
     * 模板ID
     */
    private String templateId;

    /**
     * 规则类型：1-定时，2-固定频率，3-固定延迟
     */
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
    private List<String> recipients;

    /**
     * 抄送列表
     */
    private List<String> cc;

    /**
     * 密送列表
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
     * 已执行次数
     */
    private Integer executionCount;

    /**
     * 上次执行时间
     */
    private Date lastExecutionTime;

    /**
     * 下次执行时间
     */
    private Date nextExecutionTime;

    /**
     * 启用状态
     */
    private Integer enableStatus;

    /**
     * 创建时间
     */
    private Date createdAt;

    /**
     * 更新时间
     */
    private Date updatedAt;

    /**
     * 创建人ID
     */
    private String createdBy;

    /**
     * 更新人ID
     */
    private String updatedBy;
} 