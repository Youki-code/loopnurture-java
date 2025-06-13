package org.springframework.samples.loopnurture.mail.infra.po;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

/**
 * 邮件发送规则持久化对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "email_send_rule")
@org.hibernate.annotations.Where(clause="deleted = false")
@Getter
@Setter
public class EmailSendRulePO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(name = "org_code")
    private String orgCode;

    @Column(name = "rule_id")
    private String ruleId;

    @Column(name = "rule_name")
    private String ruleName;

    @Column(name = "template_id")
    private String templateId;

    @Column(name = "rule_type")
    private Integer ruleType;

    @Column(name = "cron_expression")
    private String cronExpression;

    @Column(name = "fixed_rate")
    private Long fixedRate;

    @Column(name = "fixed_delay")
    private Long fixedDelay;

    @Column(name = "recipients")
    private String recipients;

    @Column(name = "cc")
    private String cc;

    @Column(name = "bcc")
    private String bcc;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "max_executions")
    private Integer maxExecutions;

    @Column(name = "execution_count")
    private Integer executionCount;

    @Column(name = "last_execution_time")
    private LocalDateTime lastExecutionTime;

    @Column(name = "next_execution_time")
    private LocalDateTime nextExecutionTime;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "description")
    private String description;

    /**
     * 用户过滤条件 JSON 字符串
     */
    @Column(name = "user_query")
    private String userQuery;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name="deleted",nullable=false)
    private Boolean deleted = false;
} 