package org.springframework.samples.loopnurture.mail.infra.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.samples.loopnurture.mail.domain.model.EmailSendRuleDO;
import org.springframework.samples.loopnurture.mail.domain.repository.EmailSendRuleRepository;
import org.springframework.samples.loopnurture.mail.infra.converter.EmailSendRuleConverter;
import org.springframework.samples.loopnurture.mail.infra.mapper.JpaEmailSendRuleMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 邮件发送规则仓储实现类
 */
@Repository
public class EmailSendRuleRepositoryImpl implements EmailSendRuleRepository {

    private final JpaEmailSendRuleMapper emailSendRuleMapper;
    private final EmailSendRuleConverter emailSendRuleConverter;

    @Autowired
    public EmailSendRuleRepositoryImpl(JpaEmailSendRuleMapper emailSendRuleMapper,
                                     EmailSendRuleConverter emailSendRuleConverter) {
        this.emailSendRuleMapper = emailSendRuleMapper;
        this.emailSendRuleConverter = emailSendRuleConverter;
    }

    @Override
    public EmailSendRuleDO save(EmailSendRuleDO rule) {
        var po = emailSendRuleConverter.toPO(rule);
        emailSendRuleMapper.save(po);
        return emailSendRuleConverter.toDO(po);
    }

    @Override
    public EmailSendRuleDO findById(String id) {
        return emailSendRuleMapper.findById(id)
                .map(emailSendRuleConverter::toDO)
                .orElse(null);
    }

    @Override
    public EmailSendRuleDO findByOrgCodeAndRuleId(String orgCode, String ruleId) {
        return emailSendRuleMapper.findByOrgCodeAndRuleId(orgCode, ruleId)
                .map(emailSendRuleConverter::toDO)
                .orElse(null);
    }

    @Override
    public List<EmailSendRuleDO> findActiveRulesByOrgCode(String orgCode) {
        return emailSendRuleMapper.findByOrgCodeAndIsActiveTrue(orgCode)
                .stream()
                .map(emailSendRuleConverter::toDO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<EmailSendRuleDO> findByOrgCode(String orgCode, Pageable pageable) {
        return emailSendRuleMapper.findByOrgCode(orgCode, pageable)
                .map(emailSendRuleConverter::toDO);
    }

    @Override
    public boolean existsByOrgCodeAndRuleId(String orgCode, String ruleId) {
        return emailSendRuleMapper.existsByOrgCodeAndRuleId(orgCode, ruleId);
    }

    @Override
    public void deleteById(String id) {
        emailSendRuleMapper.softDeleteById(id);
    }

    @Override
    public List<EmailSendRuleDO> findRulesForExecution(LocalDateTime now) {
        return emailSendRuleMapper.findByIsActiveTrueAndNextExecutionTimeLessThanEqual(now)
                .stream()
                .map(emailSendRuleConverter::toDO)
                .collect(Collectors.toList());
    }

    @Override
    public long countByOrgCode(String orgCode) {
        return emailSendRuleMapper.countByOrgCode(orgCode);
    }

    @Override
    public void updateExecutionInfo(String id, LocalDateTime lastExecuteTime, LocalDateTime nextExecuteTime, int executedCount) {
        emailSendRuleMapper.updateExecutionInfo(id, lastExecuteTime, nextExecuteTime, executedCount);
    }

    @Override
    public List<EmailSendRuleDO> findAll() {
        return emailSendRuleMapper.findAll().stream()
            .map(emailSendRuleConverter::toDO)
            .collect(Collectors.toList());
    }

    @Override
    public boolean existsByOrgIdAndRuleName(String orgId, String ruleName) {
        return emailSendRuleMapper.existsByOrgIdAndRuleName(orgId, ruleName);
    }

    @Override
    public boolean existsByOrgCodeAndRuleName(String orgCode, String ruleName) {
        return emailSendRuleMapper.existsByOrgCodeAndRuleName(orgCode, ruleName);
    }

    @Override
    public boolean existsById(String id) {
        return emailSendRuleMapper.existsById(id);
    }

    @Override
    public void update(String id, EmailSendRuleDO rule) {
        var po = emailSendRuleConverter.toPO(rule);
        po.setId(id);
        emailSendRuleMapper.save(po);
    }

    @Override
    public Page<EmailSendRuleDO> findByOrgId(String orgId, Pageable pageable) {
        return emailSendRuleMapper.findByOrgId(orgId, pageable)
                .map(emailSendRuleConverter::toDO);
    }
} 