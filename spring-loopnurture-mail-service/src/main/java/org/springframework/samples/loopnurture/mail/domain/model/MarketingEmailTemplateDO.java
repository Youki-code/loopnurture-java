package org.springframework.samples.loopnurture.mail.domain.model;

import lombok.Data;
import org.springframework.samples.loopnurture.mail.domain.enums.EnableStatusEnum;
import org.springframework.samples.loopnurture.mail.domain.enums.ContentTypeEnum;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * 营销邮件模板领域对象
 * 
 * 该对象代表了一个营销邮件模板的业务实体，包含了模板的基本信息和业务行为。
 * 与数据库实体的主要区别：
 * 1. 直接使用枚举类型而不是代码
 * 2. 包含了模板渲染等业务方法
 */
@Data
public class MarketingEmailTemplateDO {
    /**
     * 主键ID
     */
    private String id;

    /**
     * 组织编码
     */
    private String orgCode;

    /**
     * 模板ID，在组织内唯一
     */
    private String templateId;

    /**
     * 模板名称
     */
    private String templateName;

    /**
     * 内容类型：1-文本，2-HTML
     */
    private ContentTypeEnum contentType;

    /**
     * 模板内容
     */
    private String contentTemplate;

    /**
     * AI策略版本
     */
    private String aiStrategyVersion;

    /**
     * 启用状态：1-启用，0-禁用
     */
    private EnableStatusEnum enableStatus;

    /**
     * 扩展信息
     */
    private Map<String, Object> extendsInfo;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    /**
     * 创建人ID
     */
    private String createdBy;

    /**
     * 更新人ID
     */
    private String updatedBy;

    /**
     * 检查模板是否是HTML格式
     *
     * @return true 如果是HTML格式
     */
    public boolean isHtmlContent() {
        return contentType == ContentTypeEnum.HTML;
    }

    /**
     * 检查模板是否启用
     *
     * @return true 如果是启用状态
     */
    public boolean isEnabled() {
        return enableStatus == EnableStatusEnum.ENABLED;
    }

    /**
     * 检查模板是否禁用
     *
     * @return true 如果是禁用状态
     */
    public boolean isDisabled() {
        return enableStatus == EnableStatusEnum.DISABLED;
    }
} 