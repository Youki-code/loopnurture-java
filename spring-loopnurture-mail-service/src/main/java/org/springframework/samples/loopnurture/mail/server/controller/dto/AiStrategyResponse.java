package org.springframework.samples.loopnurture.mail.server.controller.dto;

import lombok.Data;

/**
 * AI 策略响应
 */
@Data
public class AiStrategyResponse {

    private String aiStrategyVersion;

    private Integer aiStrategyType;

    private String aiStrategyContent;
} 