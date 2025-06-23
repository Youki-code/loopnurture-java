package org.springframework.samples.loopnurture.mail.service;

import lombok.RequiredArgsConstructor;
import org.springframework.samples.loopnurture.mail.domain.model.AiStrategyDO;
import org.springframework.samples.loopnurture.mail.domain.repository.AiStrategyRepository;
import org.springframework.stereotype.Service;

/**
 * AI 策略查询服务
 */
@Service
@RequiredArgsConstructor
public class AiStrategyQueryService {

    private final AiStrategyRepository aiStrategyRepository;

    /**
     * 获取最新启用的策略内容
     *
     * @param aiStrategyType 策略类型代码
     * @return JSON 内容字符串
     */
    public AiStrategyDO getLatestEnabledStrategy(Integer aiStrategyType) {
        return aiStrategyRepository.findLatestEnabledByType(aiStrategyType);
    }
} 