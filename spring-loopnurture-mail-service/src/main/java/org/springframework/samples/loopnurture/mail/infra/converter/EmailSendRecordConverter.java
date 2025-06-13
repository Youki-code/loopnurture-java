package org.springframework.samples.loopnurture.mail.infra.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.samples.loopnurture.mail.domain.model.EmailSendRecordDO;
import org.springframework.samples.loopnurture.mail.domain.enums.EmailStatusEnum;
import org.springframework.samples.loopnurture.mail.infra.po.EmailSendRecordPO;
import org.springframework.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 邮件发送记录转换器
 * 负责在领域对象和持久化对象之间进行转换
 * 处理持久化相关的字段（如id），但不会将其暴露给领域对象
 */
@Component
@RequiredArgsConstructor
public class EmailSendRecordConverter {
    
    private final ObjectMapper objectMapper;
    private static final Logger log = LoggerFactory.getLogger(EmailSendRecordConverter.class);
    
    /**
     * 将持久化对象转换为领域对象
     */
    public EmailSendRecordDO toDO(EmailSendRecordPO po) {
        if (po == null) {
            return null;
        }

        EmailSendRecordDO entity = new EmailSendRecordDO();
        entity.setOrgCode(po.getOrgCode());
        entity.setTemplateId(po.getTemplateId());
        entity.setSender(po.getSender());
        entity.setRecipient(po.getRecipient());
        entity.setCc(po.getCc());
        entity.setBcc(po.getBcc());
        entity.setSubject(po.getSubject());
        entity.setContent(po.getContent());
        entity.setStatus(po.getStatus() != null ? EmailStatusEnum.fromValue(po.getStatus()) : null);
        entity.setErrorMessage(po.getErrorMessage());
        entity.setRetryCount(po.getRetryCount());
        entity.setSentAt(po.getSentAt());
        entity.setCreatedAt(po.getCreatedAt());
        entity.setUpdatedAt(po.getUpdatedAt());
        entity.setCreatedBy(po.getCreatedBy());
        entity.setUpdatedBy(po.getUpdatedBy());

        if (po.getVariables() != null) {
            entity.setVariables(parseVariables(po.getVariables()));
        }

        return entity;
    }

    /**
     * 将领域对象转换为持久化对象
     * @param entity 领域对象
     * @param id 可选的ID，用于更新操作
     */
    public EmailSendRecordPO toPO(EmailSendRecordDO entity, String id) {
        if (entity == null) {
            return null;
        }

        EmailSendRecordPO po = new EmailSendRecordPO();
        po.setId(id); // 仅在基础设施层处理ID
        po.setOrgCode(entity.getOrgCode());
        po.setTemplateId(entity.getTemplateId());
        po.setSender(entity.getSender());
        po.setRecipient(entity.getRecipient());
        po.setCc(entity.getCc());
        po.setBcc(entity.getBcc());
        po.setSubject(entity.getSubject());
        po.setContent(entity.getContent());
        po.setStatus(entity.getStatus() != null ? entity.getStatus().getCode() : null);
        po.setErrorMessage(entity.getErrorMessage());
        po.setRetryCount(entity.getRetryCount());
        po.setSentAt(entity.getSentAt());
        po.setCreatedAt(entity.getCreatedAt());
        po.setUpdatedAt(entity.getUpdatedAt());
        po.setCreatedBy(entity.getCreatedBy());
        po.setUpdatedBy(entity.getUpdatedBy());

        if (entity.getVariables() != null) {
            po.setVariables(serializeVariables(entity.getVariables()));
        }

        return po;
    }

    /**
     * 将领域对象转换为持久化对象（用于新建操作）
     */
    public EmailSendRecordPO toPO(EmailSendRecordDO entity) {
        return toPO(entity, null);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> parseVariables(String variables) {
        if (!StringUtils.hasText(variables)) {
            return new HashMap<>();
        }
        try {
            return objectMapper.readValue(variables, Map.class);
        } catch (JsonProcessingException e) {
            log.error("Failed to parse variables: {}", variables, e);
            return new HashMap<>();
        }
    }

    private String serializeVariables(Map<String, Object> variables) {
        if (variables == null || variables.isEmpty()) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(variables);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize variables: {}", variables, e);
            return null;
        }
    }
} 