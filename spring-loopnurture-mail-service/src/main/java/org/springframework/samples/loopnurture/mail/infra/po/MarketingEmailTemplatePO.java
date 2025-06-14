package org.springframework.samples.loopnurture.mail.infra.po;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.util.Date;

/**
 * 营销邮件模板持久化对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "marketing_email_template")
@org.hibernate.annotations.Where(clause = "deleted = false")
public class MarketingEmailTemplatePO {
    
    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    /**
     * 组织编码
     */
    @Column(name = "org_code")
    private String orgCode;

    /**
     * 模板ID，在组织内唯一
     */
    @Column(name = "template_id")
    private String templateId;

    /**
     * 模板名称
     */
    @Column(name = "template_name")
    private String templateName;

    /**
     * 内容类型：1-文本，2-HTML
     */
    @Column(name = "content_type")
    private Integer contentType;

    /**
     * 模板内容
     */
    @Column(name = "content_template")
    private String contentTemplate;

    /**
     * AI策略版本
     */
    @Column(name = "ai_strategy_version")
    private String aiStrategyVersion;

    /**
     * 启用状态：1-启用，0-禁用
     */
    @Column(name = "enable_status")
    private Integer enableStatus;

    /**
     * 扩展信息（JSON格式）
     */
    @Column(name = "extends_info")
    private String extendsInfo;

    /**
     * 创建时间
     */
    @CreationTimestamp
    @Column(name = "created_at")
    private Date createdAt;

    /**
     * 更新时间
     */
    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

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

    /**
     * 逻辑删除标记
     */
    @Column(name = "deleted", nullable = false)
    private Boolean deleted = false;
} 