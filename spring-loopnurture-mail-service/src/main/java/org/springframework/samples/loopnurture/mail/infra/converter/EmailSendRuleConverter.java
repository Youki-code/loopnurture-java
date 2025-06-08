package org.springframework.samples.loopnurture.mail.infra.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.samples.loopnurture.mail.domain.enums.RuleTypeEnum;
import org.springframework.samples.loopnurture.mail.domain.enums.RecipientTypeEnum;
import org.springframework.samples.loopnurture.mail.domain.model.EmailSendRuleDO;
import org.springframework.samples.loopnurture.mail.infra.po.EmailSendRulePO;

/**
 * 邮件发送规则转换器
 */
@Component
@RequiredArgsConstructor
public class EmailSendRuleConverter {

    /**
     * 将领域对象转换为持久化对象
     */
    public EmailSendRulePO toPO(EmailSendRuleDO domain) {
        if (domain == null) {
            return null;
        }

        EmailSendRulePO po = new EmailSendRulePO();
        po.setId(domain.getId());
        po.setOrgId(domain.getOrgId());
        po.setTemplateId(domain.getTemplateId());
        po.setRuleName(domain.getRuleName());
        po.setRuleType(domain.getRuleType().getCode());
        po.setCronExpression(domain.getCronExpression());
        po.setFixedRate(domain.getFixedRate());
        po.setFixedDelay(domain.getFixedDelay());
        po.setRecipientType(domain.getRecipientType().getCode());
        po.setRecipients(domain.getRecipients());
        po.setRecipientQuery(domain.getRecipientQuery());
        po.setCc(domain.getCc());
        po.setBcc(domain.getBcc());
        po.setVariablesQuery(domain.getVariablesQuery());
        po.setStartTime(domain.getStartTime());
        po.setEndTime(domain.getEndTime());
        po.setMaxExecutions(domain.getMaxExecutions());
        po.setExecutionCount(domain.getExecutionCount());
        po.setLastExecutionTime(domain.getLastExecutionTime());
        po.setNextExecutionTime(domain.getNextExecutionTime());
        po.setIsActive(domain.getIsActive());
        po.setDescription(domain.getDescription());
        po.setCreatedAt(domain.getCreatedAt());
        po.setUpdatedAt(domain.getUpdatedAt());
        po.setCreatedBy(domain.getCreatedBy());
        po.setUpdatedBy(domain.getUpdatedBy());
        return po;
    }

    /**
     * 将持久化对象转换为领域对象
     */
    public EmailSendRuleDO toDO(EmailSendRulePO po) {
        if (po == null) {
            return null;
        }

        EmailSendRuleDO domain = new EmailSendRuleDO();
        domain.setId(po.getId());
        domain.setOrgId(po.getOrgId());
        domain.setTemplateId(po.getTemplateId());
        domain.setRuleName(po.getRuleName());
        domain.setRuleType(RuleTypeEnum.fromCode(po.getRuleType()));
        domain.setCronExpression(po.getCronExpression());
        domain.setFixedRate(po.getFixedRate());
        domain.setFixedDelay(po.getFixedDelay());
        domain.setRecipientType(RecipientTypeEnum.fromCode(po.getRecipientType()));
        domain.setRecipients(po.getRecipients());
        domain.setRecipientQuery(po.getRecipientQuery());
        domain.setCc(po.getCc());
        domain.setBcc(po.getBcc());
        domain.setVariablesQuery(po.getVariablesQuery());
        domain.setStartTime(po.getStartTime());
        domain.setEndTime(po.getEndTime());
        domain.setMaxExecutions(po.getMaxExecutions());
        domain.setExecutionCount(po.getExecutionCount());
        domain.setLastExecutionTime(po.getLastExecutionTime());
        domain.setNextExecutionTime(po.getNextExecutionTime());
        domain.setIsActive(po.getIsActive());
        domain.setDescription(po.getDescription());
        domain.setCreatedAt(po.getCreatedAt());
        domain.setUpdatedAt(po.getUpdatedAt());
        domain.setCreatedBy(po.getCreatedBy());
        domain.setUpdatedBy(po.getUpdatedBy());
        return domain;
    }
} 