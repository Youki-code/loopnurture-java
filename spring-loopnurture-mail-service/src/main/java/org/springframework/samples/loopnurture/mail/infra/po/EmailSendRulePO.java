package org.springframework.samples.loopnurture.mail.infra.po;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.util.Date;

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
    private Long id;

    @Column(name = "org_code")
    private String orgCode;

    @Column(name = "rule_id")
    private String ruleId;

    @Column(name = "rule_name")
    private String ruleName;

    @Column(name = "template_id")
    private String templateId;

    @Column(name = "rule_type", columnDefinition = "SMALLINT")
    private Short ruleType;

    /**
     * 扩展信息(JSON)
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "extends_info", columnDefinition = "jsonb")
    private String extendsInfo;

    @Column(name = "start_time")
    private Date startTime;

    @Column(name = "end_time")
    private Date endTime;

    @Column(name = "max_executions")
    private Integer maxExecutions;

    @Column(name = "execution_count")
    private Integer executionCount;

    @Column(name = "last_execution_time")
    private Date lastExecutionTime;

    @Column(name = "next_execution_time")
    private Date nextExecutionTime;

    @Column(name = "enable_status", columnDefinition = "SMALLINT")
    private Short enableStatus;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name="deleted",nullable=false)
    private Boolean deleted = false;
} 