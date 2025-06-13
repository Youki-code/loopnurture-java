package org.springframework.samples.loopnurture.mail.infra.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.samples.loopnurture.mail.domain.model.EmailSendRuleDO;
import org.springframework.samples.loopnurture.mail.domain.enums.RuleTypeEnum;
import org.springframework.samples.loopnurture.mail.infra.po.EmailSendRulePO;
import org.springframework.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 邮件发送规则转换器
 */
@Component
@RequiredArgsConstructor
public class EmailSendRuleConverter {
    private final ObjectMapper objectMapper;
    private static final Logger log = LoggerFactory.getLogger(EmailSendRuleConverter.class);

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
        entity.setRuleType(po.getRuleType());
        entity.setCronExpression(po.getCronExpression());
        entity.setFixedRate(po.getFixedRate());
        entity.setFixedDelay(po.getFixedDelay());
        entity.setRecipients(po.getRecipients());
        entity.setCc(po.getCc());
        entity.setBcc(po.getBcc());
        entity.setStartTime(po.getStartTime());
        entity.setEndTime(po.getEndTime());
        entity.setMaxExecutions(po.getMaxExecutions());
        entity.setExecutionCount(po.getExecutionCount());
        entity.setLastExecutionTime(po.getLastExecutionTime());
        entity.setNextExecutionTime(po.getNextExecutionTime());
        entity.setIsActive(po.getIsActive());
        entity.setDescription(po.getDescription());
        entity.setCreatedAt(po.getCreatedAt());
        entity.setUpdatedAt(po.getUpdatedAt());
        entity.setCreatedBy(po.getCreatedBy());
        entity.setUpdatedBy(po.getUpdatedBy());

        if (po.getUserQuery() != null) {
            entity.setUserQuery(parseUserQuery(po.getUserQuery()));
        }

        return entity;
    }

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
        po.setRuleType(entity.getRuleType());
        po.setCronExpression(entity.getCronExpression());
        po.setFixedRate(entity.getFixedRate());
        po.setFixedDelay(entity.getFixedDelay());
        po.setRecipients(entity.getRecipients());
        po.setCc(entity.getCc());
        po.setBcc(entity.getBcc());
        po.setStartTime(entity.getStartTime());
        po.setEndTime(entity.getEndTime());
        po.setMaxExecutions(entity.getMaxExecutions());
        po.setExecutionCount(entity.getExecutionCount());
        po.setLastExecutionTime(entity.getLastExecutionTime());
        po.setNextExecutionTime(entity.getNextExecutionTime());
        po.setIsActive(entity.getIsActive());
        po.setDescription(entity.getDescription());
        po.setCreatedAt(entity.getCreatedAt());
        po.setUpdatedAt(entity.getUpdatedAt());
        po.setCreatedBy(entity.getCreatedBy());
        po.setUpdatedBy(entity.getUpdatedBy());

        if (entity.getUserQuery() != null) {
            po.setUserQuery(serializeUserQuery(entity.getUserQuery()));
        }

        return po;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> parseUserQuery(String userQuery) {
        if (!StringUtils.hasText(userQuery)) {
            return new HashMap<>();
        }
        try {
            return objectMapper.readValue(userQuery, Map.class);
        } catch (JsonProcessingException e) {
            log.error("Failed to parse user query: {}", userQuery, e);
            return new HashMap<>();
        }
    }

    private String serializeUserQuery(Map<String, Object> userQuery) {
        if (userQuery == null || userQuery.isEmpty()) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(userQuery);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize user query: {}", userQuery, e);
            return null;
        }
    }
} 