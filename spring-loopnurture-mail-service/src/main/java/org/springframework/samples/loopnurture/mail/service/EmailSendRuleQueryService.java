package org.springframework.samples.loopnurture.mail.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.samples.loopnurture.mail.domain.model.EmailSendRuleDO;
import org.springframework.samples.loopnurture.mail.domain.repository.EmailSendRuleRepository;
import org.springframework.samples.loopnurture.mail.exception.ResourceNotFoundException;

/**
 * 仅包含查询方法的邮件发送规则 Query Service
 */
@Service
@RequiredArgsConstructor
public class EmailSendRuleQueryService {

    private final EmailSendRuleRepository ruleRepository;

    public EmailSendRuleDO getRule(String ruleId) {
        EmailSendRuleDO rule = ruleRepository.findById(ruleId);
        if(rule == null){
            throw new ResourceNotFoundException("Rule not found");
        }
        return rule;
    }

    public Page<EmailSendRuleDO> pageRules(String orgCode, Pageable pageable) {
        return ruleRepository.findByOrgCode(orgCode, pageable);
    }

    public boolean ruleNameExists(String orgCode, String ruleName) {
        return ruleRepository.existsByOrgCodeAndRuleName(orgCode, ruleName);
    }

    // 兼容旧命名
    public Page<EmailSendRuleDO> getRules(String orgCode, Pageable pageable) {
        return pageRules(orgCode, pageable);
    }
} 