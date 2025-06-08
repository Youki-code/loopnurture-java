package org.springframework.samples.loopnurture.mail.infra.converter;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.samples.loopnurture.mail.domain.model.EmailRuleDO;
import org.springframework.samples.loopnurture.mail.infra.po.EmailRulePO;

/**
 * 邮件规则转换器
 */
@Component
public class EmailRuleConverter {
    
    /**
     * DO转PO
     */
    public EmailRulePO toPO(EmailRuleDO domain) {
        if (domain == null) {
            return null;
        }

        EmailRulePO po = new EmailRulePO();
        po.setId(domain.getId());
        po.setOrgCode(domain.getOrgCode());
        po.setRuleName(domain.getRuleName());
        po.setTemplateCode(domain.getTemplateCode());
        po.setUserQuery(domain.getUserQuery());
        po.setScheduleType(domain.getScheduleType());
        po.setScheduleValue(domain.getScheduleValue());
        po.setIsActive(domain.getIsActive());
        po.setLastExecutionTime(domain.getLastExecutionTime());
        po.setNextExecutionTime(domain.getNextExecutionTime());
        po.setExecutionCount(domain.getExecutionCount());
        po.setCreatedAt(domain.getCreatedAt());
        po.setUpdatedAt(domain.getUpdatedAt());
        po.setCreatedBy(domain.getCreatedBy());
        po.setUpdatedBy(domain.getUpdatedBy());
        return po;
    }

    /**
     * PO转DO
     */
    public EmailRuleDO toDO(EmailRulePO po) {
        if (po == null) {
            return null;
        }

        EmailRuleDO domain = new EmailRuleDO();
        domain.setId(po.getId());
        domain.setOrgCode(po.getOrgCode());
        domain.setRuleName(po.getRuleName());
        domain.setTemplateCode(po.getTemplateCode());
        domain.setUserQuery(po.getUserQuery());
        domain.setScheduleType(po.getScheduleType());
        domain.setScheduleValue(po.getScheduleValue());
        domain.setIsActive(po.getIsActive());
        domain.setLastExecutionTime(po.getLastExecutionTime());
        domain.setNextExecutionTime(po.getNextExecutionTime());
        domain.setExecutionCount(po.getExecutionCount());
        domain.setCreatedAt(po.getCreatedAt());
        domain.setUpdatedAt(po.getUpdatedAt());
        domain.setCreatedBy(po.getCreatedBy());
        domain.setUpdatedBy(po.getUpdatedBy());
        return domain;
    }
} 