package org.springframework.samples.loopnurture.mail.domain.model;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 营销邮件模板领域对象
 */
@Data
@Entity
@Table(name = "t_marketing_email_template")
public class MarketingEmailTemplateDO {
    /**
     * 模板ID
     */
    @Id
    @Column(name = "template_id")
    private String templateId;

    /**
     * 模板名称
     */
    @Column(name = "template_name")
    private String templateName;

    /**
     * 模板主题
     */
    @Column(name = "subject")
    private String subject;

    /**
     * 模板内容
     */
    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    /**
     * 发件人名称
     */
    @Column(name = "from_name")
    private String fromName;

    /**
     * 发件人邮箱
     */
    @Column(name = "from_email")
    private String fromEmail;

    /**
     * 模板状态：0-草稿，1-已发布，2-已禁用
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