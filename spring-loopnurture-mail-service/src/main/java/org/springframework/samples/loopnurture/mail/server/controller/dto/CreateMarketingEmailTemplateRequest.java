package org.springframework.samples.loopnurture.mail.server.controller.dto;

import lombok.Data;
import org.springframework.samples.loopnurture.mail.domain.model.vo.MarketingEmailTemplateExtendsInfoVO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 创建营销邮件模板请求
 */
@Data
public class CreateMarketingEmailTemplateRequest {

    /**
     * 模板名称
     */
    @Size(max = 100, message = "模板名称长度不能超过100")
    private String templateName;

    /**
     * 内容类型
     */
    @NotNull(message = "内容类型不能为空")
    private Integer contentType;

    /**
     * 内容模板
     */
    @NotBlank(message = "内容模板不能为空")
    private String contentTemplate;

    /**
     * 输入内容
     */
    @NotBlank(message = "输入内容不能为空")
    private String inputContent;

    /**
     * AI策略版本
     */
    @Size(max = 50, message = "AI strategy version length cannot exceed 50")
    private String aiStrategyVersion;

    /**
     * 公司名称，例如：XXX酒店
     */
    @Size(max = 100, message = "Company name length cannot exceed 100")
    private String companyName;

    /**
     * 邮件目的，例如：Promotion
     */
    @Size(max = 50, message = "Email purpose length cannot exceed 50")
    private String emailPurpose;

    /**
     * 扩展信息
     */
    private MarketingEmailTemplateExtendsInfoVO extendsInfo;

    /**
     * 邮件主题模板（可选）
     */
    @Size(max = 200, message = "Subject length cannot exceed 200")
    private String subjectTemplate;
} 