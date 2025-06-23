package org.springframework.samples.loopnurture.mail.infra.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.samples.loopnurture.mail.domain.enums.AiStrategyTypeEnum;
import org.springframework.samples.loopnurture.mail.domain.enums.EnableStatusEnum;
import org.springframework.samples.loopnurture.mail.domain.model.AiStrategyDO;
import org.springframework.samples.loopnurture.mail.infra.po.AiStrategyPO;

/**
 * AI 策略对象转换器
 */
@Component
@RequiredArgsConstructor
public class AiStrategyConverter {

    private final ObjectMapper objectMapper;

    public AiStrategyDO toDO(AiStrategyPO po) {
        if (po == null) {
            return null;
        }
        return AiStrategyDO.builder()
                .aiStrategyVersion(po.getAiStrategyVersion())
                .aiStrategyType(po.getAiStrategyType() != null ? AiStrategyTypeEnum.fromCode(po.getAiStrategyType().intValue()) : null)
                .aiStrategyContent(po.getAiStrategyContent())
                .enableStatus(po.getEnableStatus() != null ? EnableStatusEnum.fromCode(po.getEnableStatus().intValue()) : null)
                .createdAt(po.getCreatedAt())
                .deleted(po.getDeleted())
                .build();
    }

    public AiStrategyPO toPO(AiStrategyDO entity) {
        if (entity == null) {
            return null;
        }
        AiStrategyPO po = new AiStrategyPO();
        po.setAiStrategyVersion(entity.getAiStrategyVersion());
        po.setAiStrategyType(entity.getAiStrategyType() != null ? entity.getAiStrategyType().getCode().shortValue() : null);
        po.setAiStrategyContent(entity.getAiStrategyContent());
        po.setEnableStatus(entity.getEnableStatus() != null ? entity.getEnableStatus().getCode().shortValue() : null);
        po.setDeleted(entity.getDeleted() != null ? entity.getDeleted() : false);
        return po;
    }
} 