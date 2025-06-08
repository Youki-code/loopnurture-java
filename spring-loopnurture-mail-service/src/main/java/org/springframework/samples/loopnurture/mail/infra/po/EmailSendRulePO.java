package org.springframework.samples.loopnurture.mail.infra.po;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 邮件发送规则持久化对象
 */
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "email_send_rule")
public class EmailSendRulePO {
    
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "org_code", nullable = false)
    private String orgCode;

    @Column(name = "template_code", nullable = false)
    private String templateCode;

    @Column(name = "rule_name", nullable = false)
    private String ruleName;

    @Column(name = "rule_type", nullable = false)
    private Integer ruleType;

    @Column(name = "cron_expression")
    private String cronExpression;

    @Column(name = "fixed_rate")
    private Integer fixedRate;

    @Column(name = "fixed_delay")
    private Integer fixedDelay;

    @Column(name = "recipient_type", nullable = false)
    private Integer recipientType;

    @Column(name = "recipients")
    private String recipients;

    @Column(name = "recipient_query")
    private String recipientQuery;

    @Column(name = "cc")
    private String cc;

    @Column(name = "bcc")
    private String bcc;

    @Column(name = "variables_query")
    private String variablesQuery;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "max_executions")
    private Integer maxExecutions;

    @Column(name = "execution_count")
    private Integer executionCount = 0;

    @Column(name = "last_execution_time")
    private LocalDateTime lastExecutionTime;

    @Column(name = "next_execution_time")
    private LocalDateTime nextExecutionTime;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "description")
    private String description;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @CreatedBy
    @Column(name = "created_by", nullable = false, updatable = false)
    private String createdBy;

    @LastModifiedBy
    @Column(name = "updated_by", nullable = false)
    private String updatedBy;
} 