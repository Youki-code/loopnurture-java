package org.springframework.samples.loopnurture.mail.server.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 创建发送规则响应
 */
@Data
@AllArgsConstructor
public class CreateEmailSendRuleResponse {
    private String ruleId;
} 