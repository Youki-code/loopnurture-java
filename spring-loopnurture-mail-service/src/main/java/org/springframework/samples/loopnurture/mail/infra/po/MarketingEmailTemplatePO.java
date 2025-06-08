package org.springframework.samples.loopnurture.mail.infra.po;

import lombok.Data;
import jakarta.persistence.*;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

/**
 * 营销邮件模板持久化对象
 */
@Data
@Entity
@Table(name = "marketing_email_template")
@Where(clause = "deleted = 0")
public class MarketingEmailTemplatePO {
    
    /**
     * 主键ID
     */
    @Id
    private String id;

    /**
     * 组织编码
     */
    @Column(name = "org_code", nullable = false)
    private String orgCode;

    /**
     * 模板ID，在组织内唯一
     */
    @Column(name = "template_id", nullable = false, length = 50)
    private String templateId;

    /**
     * 模板名称
     */
    @Column(name = "template_name", nullable = false, length = 100)
    private String templateName;

    /**
     * 内容类型：1-文本，2-HTML
     */
    @Column(name = "content_type", nullable = false)
    private Integer contentType;

    /**
     * 模板内容
     */
    @Column(name = "content_template", nullable = false, columnDefinition = "text")
    private String contentTemplate;

    /**
     * AI策略版本
     */
    @Column(name = "ai_strategy_version")
    private String aiStrategyVersion;

    /**
     * 启用状态：1-启用，0-禁用
     */
    @Column(name = "enable_status", nullable = false)
    private Integer enableStatus;

    /**
     * 扩展信息
     */
    @Column(name = "extends_info")
    private String extendsInfo;

    /**
     * 删除标记：0-未删除，1-已删除
     */
    @Column(name = "deleted", nullable = false, columnDefinition = "smallint default 0")
    private Integer deleted;

    /**
     * 创建时间
     */
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * 创建人ID
     */
    @Column(name = "created_by", nullable = false, updatable = false)
    private String createdBy;

    /**
     * 更新人ID
     */
    @Column(name = "updated_by", nullable = false)
    private String updatedBy;
} 