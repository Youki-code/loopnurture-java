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
import org.springframework.samples.loopnurture.mail.domain.model.vo.EmailSendRecordExtendsInfoVO;

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
        entity.setStatus(po.getStatus() != null ? EmailStatusEnum.fromValue(po.getStatus().intValue()) : null);
        entity.setSentAt(po.getSendTime());
        entity.setCreatedAt(po.getCreatedAt());
        entity.setCreatedBy(po.getCreatedBy());

        // 从PO的各个字段构建扩展信息
        EmailSendRecordExtendsInfoVO info = EmailSendRecordExtendsInfoVO.builder()
                .subject(po.getSubject())
                .content(po.getContent())
                .recipient(po.getRecipients() != null ? java.util.List.of(po.getRecipients().split(",")) : null)
                .cc(po.getCc() != null ? java.util.List.of(po.getCc().split(",")) : null)
                .bcc(po.getBcc() != null ? java.util.List.of(po.getBcc().split(",")) : null)
                .errorMessage(po.getErrorMessage())
                .build();

        entity.setExtendsInfo(info);

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
        po.setStatus(entity.getStatus() != null ? entity.getStatus().getCode().shortValue() : null);
        po.setSendTime(entity.getSentAt());
        po.setCreatedAt(entity.getCreatedAt());
        po.setCreatedBy(entity.getCreatedBy());

        EmailSendRecordExtendsInfoVO info = entity.getExtendsInfo();

        if (info != null) {
            po.setSubject(info.getSubject());
            po.setContent(info.getContent());
            po.setRecipients(info.getRecipient() != null ? String.join(",", info.getRecipient()) : null);
            po.setCc(info.getCc() != null ? String.join(",", info.getCc()) : null);
            po.setBcc(info.getBcc() != null ? String.join(",", info.getBcc()) : null);
            po.setErrorMessage(info.getErrorMessage());
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

    private EmailSendRecordExtendsInfoVO parseExtendsInfo(String json) {
        if (!StringUtils.hasText(json)) {
            return null;
        }
        try {
            return objectMapper.readValue(json, EmailSendRecordExtendsInfoVO.class);
        } catch (JsonProcessingException e) {
            log.error("Failed to parse extendsInfo: {}", json, e);
            return null;
        }
    }

    private String serializeExtendsInfo(EmailSendRecordExtendsInfoVO info) {
        if (info == null) return null;
        try {
            return objectMapper.writeValueAsString(info);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize extendsInfo", e);
            return null;
        }
    }
} 