package org.springframework.samples.loopnurture.mail.domain.repository;

import org.springframework.samples.loopnurture.mail.domain.model.AiStrategyDO;

/**
 * AI 策略仓储接口
 */
public interface AiStrategyRepository {

    /**
     * 根据策略类型获取最新启用的策略
     *
     * @param aiStrategyType 策略类型代码
     * @return 最新启用策略
     */
    AiStrategyDO findLatestEnabledByType(Integer aiStrategyType);

    /**
     * 保存策略
     */
    AiStrategyDO save(AiStrategyDO strategy);
} 