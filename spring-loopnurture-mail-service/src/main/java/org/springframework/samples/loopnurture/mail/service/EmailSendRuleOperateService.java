package org.springframework.samples.loopnurture.mail.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.samples.loopnurture.mail.domain.repository.EmailSendRuleRepository;
import org.springframework.samples.loopnurture.mail.domain.repository.MarketingEmailTemplateRepository;
import org.springframework.samples.loopnurture.mail.exception.ResourceNotFoundException;
import org.springframework.samples.loopnurture.mail.exception.ValidationException;
import org.springframework.samples.loopnurture.mail.context.UserContext;
import org.springframework.samples.loopnurture.mail.domain.model.EmailSendRuleDO;
import org.springframework.samples.loopnurture.mail.domain.model.MarketingEmailTemplateDO;
import org.springframework.samples.loopnurture.mail.server.controller.dto.CreateEmailSendRuleRequest;
import org.springframework.samples.loopnurture.mail.server.controller.dto.UpdateEmailSendRuleRequest;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 仅包含写操作的发送规则 Operate Service
 */
@Service
@RequiredArgsConstructor
public class EmailSendRuleOperateService {

    private final EmailSendRuleRepository ruleRepository;

    private final MarketingEmailTemplateRepository marketingEmailTemplateRepository;



    @Transactional
    public EmailSendRuleDO createRule(CreateEmailSendRuleRequest request) {
        // 判断templateId是否存在
        MarketingEmailTemplateDO template = marketingEmailTemplateRepository.getByTemplateId(request.getTemplateId());
        if (template == null) {
            throw new ValidationException("Template not found");
        }
        String orgCode = UserContext.getOrgCode();
        checkDuplicateName(orgCode, request.getRuleName(), null);

        EmailSendRuleDO rule = EmailSendRuleDO.builder()
                .id(UUID.randomUUID().toString())
                .orgCode(orgCode)
                .ruleId(UUID.randomUUID().toString())
                .ruleName(request.getRuleName())
                .templateId(request.getTemplateId())
                .ruleType(request.getRuleType())
                .cronExpression(request.getCronExpression())
                .fixedRate(request.getFixedRate())
                .fixedDelay(request.getFixedDelay())
                .recipients(request.getRecipients() != null ? String.join(",", request.getRecipients()) : null)
                .cc(request.getCc() != null ? String.join(",", request.getCc()) : null)
                .bcc(request.getBcc() != null ? String.join(",", request.getBcc()) : null)
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .maxExecutions(request.getMaxExecutions())
                .executionCount(0)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .createdBy(UserContext.getUserId())
                .isActive(true)
                .build();

        return ruleRepository.save(rule);
    }

    @Transactional
    public EmailSendRuleDO modifyRule(UpdateEmailSendRuleRequest request) {
        EmailSendRuleDO rule = ruleRepository.findById(request.getRuleId());
        if(rule==null){
            throw new ResourceNotFoundException("Rule not found");
        }

        checkDuplicateName(rule.getOrgCode(), request.getRuleName(), rule.getId());

        rule.setRuleName(request.getRuleName());
        rule.setTemplateId(request.getTemplateId());
        rule.setRuleType(request.getRuleType());
        rule.setCronExpression(request.getCronExpression());
        rule.setFixedRate(request.getFixedRate());
        rule.setFixedDelay(request.getFixedDelay());
        rule.setRecipients(request.getRecipients() != null ? String.join(",", request.getRecipients()) : null);
        rule.setCc(request.getCc() != null ? String.join(",", request.getCc()) : null);
        rule.setBcc(request.getBcc() != null ? String.join(",", request.getBcc()) : null);
        rule.setStartTime(request.getStartTime());
        rule.setEndTime(request.getEndTime());
        rule.setMaxExecutions(request.getMaxExecutions());
        rule.setUpdatedAt(LocalDateTime.now());
        rule.setUpdatedBy(UserContext.getUserId());

        return ruleRepository.save(rule);
    }

    @Transactional
    public void deleteRule(String ruleId) {
        ruleRepository.deleteById(ruleId);
    }

    @Transactional
    public void executeRule(String ruleId) {
        throw new UnsupportedOperationException("executeRule not implemented in OperateService yet");
    }

    private void checkDuplicateName(String orgCode, String ruleName, String excludeId) {
        if (ruleName == null) return;
        boolean exists = ruleRepository.existsByOrgCodeAndRuleName(orgCode, ruleName);
        if (exists && excludeId == null) {
            throw new ValidationException("Rule name already exists");
        }
    }
} 