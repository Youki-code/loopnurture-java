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

import java.util.Date;
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
        if (ruleRepository.findByOrgCodeAndRuleName(rule.getOrgCode(), rule.getRuleName()) != null) {
            throw new ValidationException("Rule name already exists");
        }

        // 设置初始状态
        rule.setExecutionCount(0);
        rule.setCreatedAt(new Date());
        rule.setUpdatedAt(new Date());

        return ruleRepository.save(rule);
    }

    /**
     * 更新规则
     */
    @Transactional
    public EmailSendRuleDO modifyRule(UpdateEmailSendRuleRequest request) {
        EmailSendRuleDO rule = getRule(request.getRuleId());
        // 检查规则是否存在
        if (rule == null) {
            throw new ResourceNotFoundException("Rule not found");
        }

        // 检查规则名称是否已存在（排除自身）
        EmailSendRuleDO existingRule = ruleRepository.findByOrgCodeAndRuleName(rule.getOrgCode(), rule.getRuleName());
        if (existingRule != null && !existingRule.getRuleId().equals(rule.getRuleId())) {
            throw new ValidationException("Rule name already exists");
        }

        rule.setUpdatedAt(new Date());
        return ruleRepository.save(rule);
    }

    /**
     * 删除规则
     */
    @Transactional
    public void deleteRule(String ruleId) {
        EmailSendRuleDO exists = ruleRepository.findByRuleId(ruleId);
        if (exists == null) {
            throw new ResourceNotFoundException("Rule not found");
        }
        ruleRepository.deleteByRuleId(ruleId);
    }

    /**
     * 获取规则详情
     */
    public EmailSendRuleDO getRule(String ruleId) {
        EmailSendRuleDO rule = ruleRepository.findByRuleId(ruleId);
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
        return ruleRepository.findByOrgCodeAndRuleName(orgCode, ruleName) != null;
    }

    /**
     * 执行规则
     */
    @Transactional
    public void executeRule(String ruleId) {
        EmailSendRuleDO rule = getRule(ruleId);
        if (!rule.canExecute()) {
            throw new ValidationException("Rule is not executable");
        }

        // TODO: 实现规则执行逻辑
        
        // 更新执行信息
        Date now = new Date();
        Date nextExecutionTime = calculateNextExecutionTime(rule);
        rule.updateExecutionInfo(now, nextExecutionTime);
        ruleRepository.save(rule);
    }

    /**
     * 计算下次执行时间
     */
    private Date calculateNextExecutionTime(EmailSendRuleDO rule) {
        // TODO: 根据规则类型实现具体的计算逻辑
        return new Date(System.currentTimeMillis() + 3600_000);
    }

    /**
     * 获取需要执行的规则列表
     */
    public List<EmailSendRuleDO> findRulesForExecution(Date now) {
        return ruleRepository.findRulesForExecution(now);
    }

} 