package org.springframework.samples.loopnurture.mail.infra.converter;

import org.springframework.stereotype.Component;
import org.springframework.samples.loopnurture.mail.domain.model.EmailSendRecordDO;
import org.springframework.samples.loopnurture.mail.infra.po.EmailSendRecordPO;

/**
 * 邮件发送记录转换器
 */
@Component
public class EmailSendRecordConverter {
    
    /**
     * PO转DO
     */
    public EmailSendRecordDO toDO(EmailSendRecordPO po) {
        if (po == null) {
            return null;
        }
        
        return EmailSendRecordDO.builder()
            .id(po.getId())
            .orgCode(po.getOrgCode())
            .templateCode(po.getTemplateCode())
            .sender(po.getSender())
            .recipient(po.getRecipient())
            .cc(po.getCc())
            .bcc(po.getBcc())
            .subject(po.getSubject())
            .content(po.getContent())
            .variables(po.getVariables())
            .status(po.getStatus())
            .errorMessage(po.getErrorMessage())
            .retryCount(po.getRetryCount())
            .sentAt(po.getSentAt())
            .createdAt(po.getCreatedAt())
            .updatedAt(po.getUpdatedAt())
            .createdBy(po.getCreatedBy())
            .updatedBy(po.getUpdatedBy())
            .build();
    }

    /**
     * DO转PO
     */
    public EmailSendRecordPO toPO(EmailSendRecordDO domain) {
        if (domain == null) {
            return null;
        }

        var po = new EmailSendRecordPO();
        po.setId(domain.getId());
        po.setOrgCode(domain.getOrgCode());
        po.setTemplateCode(domain.getTemplateCode());
        po.setSender(domain.getSender());
        po.setRecipient(domain.getRecipient());
        po.setCc(domain.getCc());
        po.setBcc(domain.getBcc());
        po.setSubject(domain.getSubject());
        po.setContent(domain.getContent());
        po.setVariables(domain.getVariables());
        po.setStatus(domain.getStatus());
        po.setErrorMessage(domain.getErrorMessage());
        po.setRetryCount(domain.getRetryCount());
        po.setSentAt(domain.getSentAt());
        po.setCreatedAt(domain.getCreatedAt());
        po.setUpdatedAt(domain.getUpdatedAt());
        po.setCreatedBy(domain.getCreatedBy());
        po.setUpdatedBy(domain.getUpdatedBy());
        return po;
    }
} 