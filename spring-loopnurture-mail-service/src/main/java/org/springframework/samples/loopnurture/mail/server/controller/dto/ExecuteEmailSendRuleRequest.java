package org.springframework.samples.loopnurture.mail.server.controller.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

/**
 * 执行邮件发送规则请求DTO
 */
@Data
public class ExecuteEmailSendRuleRequest {
    
    /**
     * 规则ID
     */
    @NotBlank(message = "Rule ID cannot be null")
    private String ruleId;
} 