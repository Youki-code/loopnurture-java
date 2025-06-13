package org.springframework.samples.loopnurture.mail.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.samples.loopnurture.mail.server.controller.dto.UpdateEmailSendRuleRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.samples.loopnurture.mail.domain.model.EmailSendRuleDO;
import org.springframework.samples.loopnurture.mail.domain.repository.EmailSendRuleRepository;
import org.springframework.samples.loopnurture.mail.exception.ResourceNotFoundException;
import org.springframework.samples.loopnurture.mail.exception.ValidationException;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 邮件发送规则服务
 */
@Service
@RequiredArgsConstructor
public class EmailSendRuleService {

    private final EmailSendRuleRepository ruleRepository;

    /**
     * 创建规则
     */
    @Transactional
    public EmailSendRuleDO createRule(EmailSendRuleDO rule) {
        // 检查规则名称是否已存在
        if (ruleRepository.existsByOrgCodeAndRuleName(rule.getOrgCode(), rule.getRuleName())) {
            throw new ValidationException("Rule name already exists");
        }

        // 设置初始状态
        rule.setExecutionCount(0);
        rule.setCreatedAt(LocalDateTime.now());
        rule.setUpdatedAt(LocalDateTime.now());

        return ruleRepository.save(rule);
    }

    /**
     * 更新规则
     */
    @Transactional
    public EmailSendRuleDO modifyRule(UpdateEmailSendRuleRequest request) {
        EmailSendRuleDO rule = getRule(request.getRuleId());
        // 检查规则是否存在
        if (!ruleRepository.existsById(rule.getId())) {
            throw new ResourceNotFoundException("Rule not found");
        }

        // 检查规则名称是否已存在（排除自身）
        EmailSendRuleDO existingRule = ruleRepository.findByOrgCodeAndRuleId(rule.getOrgCode(), rule.getRuleId());
        if(existingRule==null){
            throw new ResourceNotFoundException("Rule not found");
        }
        
        if (!existingRule.getId().equals(rule.getId()) && 
            ruleRepository.existsByOrgCodeAndRuleName(rule.getOrgCode(), rule.getRuleName())) {
            throw new ValidationException("Rule name already exists");
        }

        rule.setUpdatedAt(LocalDateTime.now());
        return ruleRepository.save(rule);
    }

    /**
     * 删除规则
     */
    @Transactional
    public void deleteRule(String ruleId) {
        if (!ruleRepository.existsById(ruleId)) {
            throw new ResourceNotFoundException("Rule not found");
        }
        ruleRepository.deleteById(ruleId);
    }

    /**
     * 获取规则详情
     */
    public EmailSendRuleDO getRule(String ruleId) {
        EmailSendRuleDO rule = ruleRepository.findById(ruleId);
        if(rule==null){
            throw new ResourceNotFoundException("Rule not found");
        }
        return rule;
    }

    /**
     * 分页查询组织的规则列表
     */
    public Page<EmailSendRuleDO> getRules(String orgCode, Pageable pageable) {
        return ruleRepository.findByOrgCode(orgCode, pageable);
    }

    /**
     * 检查同组织下是否存在同名规则
     */
    public boolean ruleNameExists(String orgCode, String ruleName) {
        return ruleRepository.existsByOrgCodeAndRuleName(orgCode, ruleName);
    }

    /**
     * 执行规则
     */
    @Transactional
    public void executeRule(String ruleId) {
        EmailSendRuleDO rule = getRule(ruleId);
        if (!rule.isExecutable()) {
            throw new ValidationException("Rule is not executable");
        }

        // TODO: 实现规则执行逻辑
        
        // 更新执行信息
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextExecutionTime = calculateNextExecutionTime(rule);
        rule.recordExecution(nextExecutionTime);
        ruleRepository.save(rule);
    }

    /**
     * 计算下次执行时间
     */
    private LocalDateTime calculateNextExecutionTime(EmailSendRuleDO rule) {
        // TODO: 根据规则类型实现具体的计算逻辑
        return LocalDateTime.now().plusHours(1);
    }

    /**
     * 获取需要执行的规则列表
     */
    public List<EmailSendRuleDO> findRulesForExecution(LocalDateTime now) {
        return ruleRepository.findRulesForExecution(now);
    }

    /**
     * 创建规则（DTO 版）
     */
    @Transactional
    public EmailSendRuleDO createRule(org.springframework.samples.loopnurture.mail.server.controller.dto.CreateEmailSendRuleRequest request) {
        EmailSendRuleDO rule = EmailSendRuleDO.builder()
                .orgCode(org.springframework.samples.loopnurture.mail.context.UserContext.getOrgCode())
                .ruleId(java.util.UUID.randomUUID().toString())
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
                .createdBy(org.springframework.samples.loopnurture.mail.context.UserContext.getUserId())
                .isActive(true)
                .build();
        return createRule(rule);
    }
} 