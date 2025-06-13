package org.springframework.samples.loopnurture.mail.server.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 通用请求，仅包含模板ID
 */
@Data
public class TemplateIdRequest {
    @NotBlank(message = "templateId cannot be blank")
    private String templateId;
} 