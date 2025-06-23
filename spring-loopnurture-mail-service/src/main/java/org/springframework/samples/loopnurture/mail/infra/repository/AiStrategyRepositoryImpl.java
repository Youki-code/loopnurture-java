package org.springframework.samples.loopnurture.mail.infra.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.samples.loopnurture.mail.domain.enums.EnableStatusEnum;
import org.springframework.samples.loopnurture.mail.domain.model.AiStrategyDO;
import org.springframework.samples.loopnurture.mail.domain.repository.AiStrategyRepository;
import org.springframework.samples.loopnurture.mail.infra.converter.AiStrategyConverter;
import org.springframework.samples.loopnurture.mail.infra.mapper.JpaAiStrategyMapper;
import org.springframework.stereotype.Repository;

/**
 * AI 策略仓储实现
 */
@Repository
@RequiredArgsConstructor
public class AiStrategyRepositoryImpl implements AiStrategyRepository {

    private final JpaAiStrategyMapper jpaMapper;
    private final AiStrategyConverter converter;

    @Override
    public AiStrategyDO findLatestEnabledByType(Integer aiStrategyType) {
        return jpaMapper.findTopByTypeAndStatus(aiStrategyType.shortValue(), EnableStatusEnum.ENABLED.getCode().shortValue())
                .map(converter::toDO)
                .orElse(null);
    }

    @Override
    public AiStrategyDO save(AiStrategyDO strategy) {
        return converter.toDO(jpaMapper.save(converter.toPO(strategy)));
    }
} 