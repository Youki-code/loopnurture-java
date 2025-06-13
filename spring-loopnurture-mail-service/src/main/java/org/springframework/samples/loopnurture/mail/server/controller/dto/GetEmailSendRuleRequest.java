package org.springframework.samples.loopnurture.mail.server.controller.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

/**
 * 获取邮件发送规则详情请求DTO
 */
@Data
public class GetEmailSendRuleRequest {
    
    /**
     * 规则ID
     */
    @NotBlank(message = "Rule ID cannot be null")
    private String ruleId;
} 