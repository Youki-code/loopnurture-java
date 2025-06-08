package org.springframework.samples.loopnurture.mail.server.controller.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Map;

/**
 * 创建营销邮件模板请求DTO
 */
@Data
public class CreateMarketingEmailTemplateRequest {

    /**
     * 模板名称（可选，如果不传则由系统自动生成）
     */
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
     * AI策略版本
     */
    @Size(max = 50, message = "AI strategy version length cannot exceed 50")
    private String aiStrategyVersion;

    /**
     * 扩展信息
     */
    private Map<String, Object> extendsInfo;
} 