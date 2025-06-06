package org.springframework.samples.loopnurture.mail.domain.model;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 营销邮件规则领域对象
 */
@Data
@Entity
@Table(name = "t_marketing_email_rule")
public class MarketingEmailRuleDO {
    /**
     * 规则ID
     */
    @Id
    @Column(name = "rule_id")
    private String ruleId;

    /**
     * 规则名称
     */
    @Column(name = "rule_name")
    private String ruleName;

    /**
     * 关联的模板ID
     */
    @Column(name = "template_id")
    private String templateId;

    /**
     * 触发条件(JSON格式)
     */
    @Column(name = "trigger_condition", columnDefinition = "JSONB")
    private String triggerCondition;

    /**
     * 目标用户群体(JSON格式)
     */
    @Column(name = "target_audience", columnDefinition = "JSONB")
    private String targetAudience;

    /**
     * 发送时间配置(JSON格式)
     * 包含：立即发送/定时发送/周期发送等
     */
    @Column(name = "send_time_config", columnDefinition = "JSONB")
    private String sendTimeConfig;

    /**
     * 规则状态：0-未启用，1-已启用，2-已暂停
     */
    @Column(name = "status")
    private Integer status;

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