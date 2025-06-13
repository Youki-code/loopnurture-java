package org.springframework.samples.loopnurture.mail.server.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 请求邮件发送规则详情请求DTO
 */
@Data
public class DetailEmailSendRuleRequest {
    
    /**
     * 规则ID
     */
    @NotBlank(message = "Rule ID cannot be null")
    private String ruleId;
} 