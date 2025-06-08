package org.springframework.samples.loopnurture.mail.server.controller.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Map;

/**
 * 营销邮件模板请求DTO
 */
@Data
public class MarketingEmailTemplateRequest {
    
    /**
     * 模板ID，更新时必填
     */
    private Long id;

    /**
     * 模板ID，唯一标识
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
     * 模板类型：1-系统模板，2-自定义模板
     */
    @NotNull(message = "Template type cannot be empty")
    private Integer templateType;

    /**
     * 内容类型：1-文本，2-HTML
     */
    @NotNull(message = "Content type cannot be empty")
    private Integer contentType;

    /**
     * 模板主题
     */
    @NotBlank(message = "Template subject cannot be empty")
    @Size(max = 200, message = "Template subject length cannot exceed 200")
    private String subject;

    /**
     * 模板内容
     */
    @NotBlank(message = "Template content cannot be empty")
    private String content;

    /**
     * 扩展信息
     */
    private Map<String, Object> extendsInfo;

    /**
     * 模板描述
     */
    private String description;
} 