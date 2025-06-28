package org.springframework.samples.loopnurture.mail.server.controller.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Map;

/**
 * 修改营销邮件模板请求DTO
 */
@Data
public class ModifyMarketingEmailTemplateRequest {

    /**
     * 模板ID
     */
    @NotBlank(message = "Template ID cannot be empty")
    @Size(max = 50, message = "Template ID length cannot exceed 50")
    private String templateId;

    /**
     * 模板名称
     */
    @NotBlank(message = "Template name cannot be empty")
    @Size(max = 100, message = "Template name length cannot exceed 100")
    private String templateName;

    /**
     * 内容类型：1-文本，2-HTML
     */
    @NotNull(message = "Content type cannot be empty")
    private Integer contentType;

    /**
     * 模板内容
     */
    @NotBlank(message = "Template content cannot be empty")
    private String contentTemplate;

    /**
     * 输入内容
     */
    @NotBlank(message = "Input content cannot be empty")
    private String inputContent;

    /**
     * 启用状态：1-启用，0-禁用
     */
    private Integer enableStatus;

    /**
     * AI策略版本
     */
    @Size(max = 50, message = "AI strategy version length cannot exceed 50")
    private String aiStrategyVersion;

    /**
     * 邮件主题模板（可选）
     */
    @Size(max = 200, message = "Subject length cannot exceed 200")
    private String subjectTemplate;

} 