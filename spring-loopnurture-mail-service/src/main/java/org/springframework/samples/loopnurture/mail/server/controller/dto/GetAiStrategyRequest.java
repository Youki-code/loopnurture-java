package org.springframework.samples.loopnurture.mail.server.controller.dto;

import lombok.Data;

/**
 * 获取 AI 策略请求
 */
@Data
public class GetAiStrategyRequest {
    /**
     * 策略类型代码
     */
    private Integer aiStrategyType;
} 