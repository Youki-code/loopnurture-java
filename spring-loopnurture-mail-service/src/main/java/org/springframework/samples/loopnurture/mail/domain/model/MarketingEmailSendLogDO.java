package org.springframework.samples.loopnurture.mail.domain.model;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 营销邮件发送日志领域对象
 */
@Data
@Entity
@Table(name = "t_marketing_email_send_log")
public class MarketingEmailSendLogDO {
    /**
     * 日志ID
     */
    @Id
    @Column(name = "log_id")
    private String logId;

    /**
     * 规则ID
     */
    @Column(name = "rule_id")
    private String ruleId;

    /**
     * 模板ID
     */
    @Column(name = "template_id")
    private String templateId;

    /**
     * 收件人邮箱
     */
    @Column(name = "to_email")
    private String toEmail;

    /**
     * 收件人名称
     */
    @Column(name = "to_name")
    private String toName;

    /**
     * 发件人邮箱
     */
    @Column(name = "from_email")
    private String fromEmail;

    /**
     * 发件人名称
     */
    @Column(name = "from_name")
    private String fromName;

    /**
     * 邮件主题
     */
    @Column(name = "subject")
    private String subject;

    /**
     * 邮件内容
     */
    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    /**
     * 发送状态：0-待发送，1-发送中，2-发送成功，3-发送失败
     */
    @Column(name = "status")
    private Integer status;

    /**
     * 失败原因
     */
    @Column(name = "fail_reason", columnDefinition = "TEXT")
    private String failReason;

    /**
     * 重试次数
     */
    @Column(name = "retry_count")
    private Integer retryCount;

    /**
     * 下次重试时间
     */
    @Column(name = "next_retry_time")
    private LocalDateTime nextRetryTime;

    /**
     * 所属组织ID
     */
    @Column(name = "org_id")
    private String orgId;

    /**
     * 创建时间
     */
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * 创建人ID
     */
    @Column(name = "created_by")
    private String createdBy;

    /**
     * 更新人ID
     */
    @Column(name = "updated_by")
    private String updatedBy;
} 