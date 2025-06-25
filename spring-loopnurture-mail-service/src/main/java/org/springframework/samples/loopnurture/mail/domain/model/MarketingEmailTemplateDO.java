package org.springframework.samples.loopnurture.mail.domain.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.samples.loopnurture.mail.domain.enums.EnableStatusEnum;
import org.springframework.samples.loopnurture.mail.context.UserContext;
import org.springframework.samples.loopnurture.mail.domain.enums.ContentTypeEnum;
import org.springframework.samples.loopnurture.mail.domain.model.vo.MarketingEmailTemplateExtendsInfoVO;
import java.util.Date;

/**
 * 营销邮件模板领域对象
 * 
 * 该对象代表了一个营销邮件模板的业务实体，包含了模板的基本信息和业务行为。
 * 与数据库实体的主要区别：
 * 1. 直接使用枚举类型而不是代码
 * 2. 包含了模板渲染等业务方法
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarketingEmailTemplateDO {

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
     * 公司名称，例如：XXX酒店
     */
    private String companyName;

    /**
     * 邮件目的，例如：Promotion
     */
    private String emailPurpose;

    /**
     * 启用状态：1-启用，0-禁用
     */
    private EnableStatusEnum enableStatus;

    /**
     * 扩展信息（JSON格式）
     */
    private MarketingEmailTemplateExtendsInfoVO extendsInfo;

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


    /**************************操作方法**************************/


    /**
     * 修改模板
     * @param templateName 模板名称
     * @param contentType 内容类型
     * @param contentTemplate 模板内容
     * @param aiStrategyVersion AI策略版本
     * @param enableStatus 启用状态
     * @param inputContent 用户输入内容
     */
    public void modifyTemplate(String templateName, ContentTypeEnum contentType, String contentTemplate, String aiStrategyVersion, EnableStatusEnum enableStatus, String inputContent) {
        this.templateName = templateName;
        this.contentType = contentType;
        this.contentTemplate = contentTemplate;
        this.aiStrategyVersion = aiStrategyVersion;
        this.enableStatus = enableStatus;
        if (this.extendsInfo == null) {
            this.extendsInfo = new MarketingEmailTemplateExtendsInfoVO();
        }
        this.extendsInfo.setInputContent(inputContent);
        this.updatedAt = new Date();
        this.updatedBy = UserContext.getUserId();
    }

    /**
     * 渲染模板内容
     * @param variables 模板变量
     * @return 渲染后的内容
     */
    public String renderContent(java.util.Map<String, Object> variables) {
        if (contentTemplate == null || contentTemplate.trim().isEmpty()) {
            return "";
        }
        
        String result = contentTemplate;
        if (variables != null) {
            for (java.util.Map.Entry<String, Object> entry : variables.entrySet()) {
                String key = "\\{\\{" + entry.getKey() + "\\}\\}";
                String value = entry.getValue() != null ? entry.getValue().toString() : "";
                result = result.replaceAll(key, value);
            }
        }
        
        return result;
    }

    /**
     * 验证模板配置
     */
    public void validate() {
        if (orgCode == null || orgCode.trim().isEmpty()) {
            throw new IllegalArgumentException("组织编码不能为空");
        }
        
        if (templateId == null || templateId.trim().isEmpty()) {
            throw new IllegalArgumentException("模板ID不能为空");
        }
        
        if (templateName == null || templateName.trim().isEmpty()) {
            throw new IllegalArgumentException("模板名称不能为空");
        }
        
        if (contentType == null) {
            throw new IllegalArgumentException("内容类型不能为空");
        }
        
        if (contentTemplate == null || contentTemplate.trim().isEmpty()) {
            throw new IllegalArgumentException("模板内容不能为空");
        }
    }

    /**
     * 启用模板
     */
    public void enable() {
        this.enableStatus = EnableStatusEnum.ENABLED;
    }

    /**
     * 禁用模板
     */
    public void disable() {
        this.enableStatus = EnableStatusEnum.DISABLED;
    }

    /**
     * 检查模板是否可用
     */
    public boolean isEnabled() {
        return enableStatus == EnableStatusEnum.ENABLED;
    }

    /**
     * 创建新模板时的初始化
     */
    public static MarketingEmailTemplateDO create(String orgCode, String templateId, 
                                                String templateName, ContentTypeEnum contentType,
                                                String contentTemplate, String aiStrategyVersion,
                                                String companyName, String emailPurpose) {
        MarketingEmailTemplateDO template = MarketingEmailTemplateDO.builder()
                .orgCode(orgCode)
                .templateId(templateId)
                .templateName(templateName)
                .contentType(contentType)
                .contentTemplate(contentTemplate)
                .aiStrategyVersion(aiStrategyVersion)
                .companyName(companyName)
                .emailPurpose(emailPurpose)
                .enableStatus(EnableStatusEnum.ENABLED)
                .createdAt(new Date())
                .updatedAt(new Date())
                .createdBy(UserContext.getUserId())
                .updatedBy(UserContext.getUserId())
                .build();
        
        template.validate();
        return template;
    }

    /**************************查询方法**************************/

    /**
     * 检查模板是否是HTML格式
     *
     * @return true 如果是HTML格式
     */
    public boolean isHtml() {
        return contentType == ContentTypeEnum.HTML;
    }

    /**
     * 检查模板是否禁用
     *
     * @return true 如果是禁用状态
     */
    public boolean isDisabled() {
        return enableStatus == EnableStatusEnum.DISABLED;
    }

    public boolean isAvailable() {
        return isEnabled();
    }

    /**
     * 获取模板代码
     */
    public String getTemplateCode() {
        return templateId;
    }

    /**
     * 获取主题模板
     */
    public String getSubjectTemplate() {
        return extendsInfo != null ? extendsInfo.getSubjectTemplate() : null;
    }

    public boolean isHtmlContent() {
        return contentType == ContentTypeEnum.HTML;
    }
} 