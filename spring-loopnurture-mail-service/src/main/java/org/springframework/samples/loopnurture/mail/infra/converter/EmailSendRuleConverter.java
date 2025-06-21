package org.springframework.samples.loopnurture.mail.infra.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.samples.loopnurture.mail.domain.model.EmailSendRuleDO;
import org.springframework.samples.loopnurture.mail.infra.po.EmailSendRulePO;
import org.springframework.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 邮件发送规则转换器
 */
@Component
@RequiredArgsConstructor
public class EmailSendRuleConverter {
    private final ObjectMapper objectMapper;
    private static final Logger log = LoggerFactory.getLogger(EmailSendRuleConverter.class);

    /**
     * 将持久化对象转换为领域对象
     */
    public EmailSendRuleDO toDO(EmailSendRulePO po) {
        if (po == null) {
            return null;
        }

        EmailSendRuleDO entity = new EmailSendRuleDO();
        entity.setId(po.getId());
        entity.setOrgCode(po.getOrgCode());
        entity.setRuleId(po.getRuleId());
        entity.setRuleName(po.getRuleName());
        entity.setTemplateId(po.getTemplateId());
        entity.setRuleType(po.getRuleType() != null ? po.getRuleType().intValue() : null);
        entity.setStartTime(po.getStartTime());
        entity.setEndTime(po.getEndTime());
        entity.setMaxExecutions(po.getMaxExecutions());
        entity.setExecutionCount(po.getExecutionCount());
        entity.setLastExecutionTime(po.getLastExecutionTime());
        entity.setNextExecutionTime(po.getNextExecutionTime());
        entity.setCreatedAt(po.getCreatedAt());
        entity.setUpdatedAt(po.getUpdatedAt());
        entity.setCreatedBy(po.getCreatedBy());
        entity.setUpdatedBy(po.getUpdatedBy());

        // 扩展信息 JSON -> VO
        if (StringUtils.hasText(po.getExtendsInfo())) {
            try {
                entity.setExtendsInfo(objectMapper.readValue(po.getExtendsInfo(),
                        org.springframework.samples.loopnurture.mail.domain.model.vo.EmailSendRuleExtendsInfoVO.class));
            } catch (JsonProcessingException e) {
                log.error("Failed to parse extendsInfo JSON: {}", po.getExtendsInfo(), e);
            }
        }

        // 启用状态
        entity.setEnableStatus(org.springframework.samples.loopnurture.mail.domain.enums.EnableStatusEnum.fromCode(po.getEnableStatus() != null ? po.getEnableStatus().intValue() : null));

        return entity;
    }

    /**
     * 将领域对象转换为持久化对象
     */
    public EmailSendRulePO toPO(EmailSendRuleDO entity) {
        if (entity == null) {
            return null;
        }

        EmailSendRulePO po = new EmailSendRulePO();
        po.setId(entity.getId());
        po.setOrgCode(entity.getOrgCode());
        po.setRuleId(entity.getRuleId());
        po.setRuleName(entity.getRuleName());
        po.setTemplateId(entity.getTemplateId());
        po.setRuleType(entity.getRuleType() != null ? entity.getRuleType().shortValue() : null);
        po.setStartTime(entity.getStartTime());
        po.setEndTime(entity.getEndTime());
        po.setMaxExecutions(entity.getMaxExecutions());
        po.setExecutionCount(entity.getExecutionCount());
        po.setLastExecutionTime(entity.getLastExecutionTime());
        po.setNextExecutionTime(entity.getNextExecutionTime());
        po.setCreatedAt(entity.getCreatedAt());
        po.setUpdatedAt(entity.getUpdatedAt());
        po.setCreatedBy(entity.getCreatedBy());
        po.setUpdatedBy(entity.getUpdatedBy());

        // 扩展信息 VO -> JSON
        if (entity.getExtendsInfo() != null) {
            try {
                po.setExtendsInfo(objectMapper.writeValueAsString(entity.getExtendsInfo()));
            } catch (JsonProcessingException e) {
                log.error("Failed to serialize extendsInfo: {}", entity.getExtendsInfo(), e);
            }
        }

        // 启用状态
        po.setEnableStatus(entity.getEnableStatus() != null ? entity.getEnableStatus().getCode().shortValue() : null);

        return po;
    }
} 