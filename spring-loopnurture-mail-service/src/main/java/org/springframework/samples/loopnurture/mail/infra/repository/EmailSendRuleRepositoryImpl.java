package org.springframework.samples.loopnurture.mail.infra.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.samples.loopnurture.mail.domain.model.EmailSendRuleDO;
import org.springframework.samples.loopnurture.mail.domain.repository.EmailSendRuleRepository;
import org.springframework.samples.loopnurture.mail.domain.repository.dto.EmailSendRulePageQueryDTO;
import org.springframework.samples.loopnurture.mail.infra.converter.EmailSendRuleConverter;
import org.springframework.samples.loopnurture.mail.infra.mapper.JpaEmailSendRuleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.PageRequest;
import org.springframework.samples.loopnurture.mail.infra.po.EmailSendRulePO;
import org.springframework.samples.loopnurture.mail.domain.enums.EnableStatusEnum;

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
        // upsert by (orgCode, ruleId)
        org.springframework.samples.loopnurture.mail.infra.po.EmailSendRulePO po;
        if (rule.getOrgCode()!=null && rule.getRuleId()!=null) {
            java.util.Optional<org.springframework.samples.loopnurture.mail.infra.po.EmailSendRulePO> existing =
                    emailSendRuleMapper.findByOrgCodeAndRuleId(rule.getOrgCode(), rule.getRuleId());
            po = emailSendRuleConverter.toPO(rule);
            existing.ifPresent(value -> po.setId(value.getId()));
        } else {
            po = emailSendRuleConverter.toPO(rule);
        }
        emailSendRuleMapper.save(po);
        return emailSendRuleConverter.toDO(po);
    }

    @Override
    public EmailSendRuleDO findById(Long id) {
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
    public Page<EmailSendRuleDO> findByOrgCode(String orgCode, Pageable pageable) {
        return emailSendRuleMapper.findByOrgCode(orgCode, pageable)
                .map(emailSendRuleConverter::toDO);
    }

    @Override
    public Page<EmailSendRuleDO> findByOrgCodeAndTemplateId(String orgCode, String templateId, Pageable pageable) {
        return emailSendRuleMapper.findByOrgCodeAndTemplateId(orgCode, templateId, pageable)
                .map(emailSendRuleConverter::toDO);
    }

    @Override
    public Page<EmailSendRuleDO> findByOrgCodeAndRuleType(String orgCode, Integer ruleType, Pageable pageable) {
        return emailSendRuleMapper.findByOrgCodeAndRuleType(orgCode, ruleType.shortValue(), pageable)
                .map(emailSendRuleConverter::toDO);
    }

    @Override
    public Page<EmailSendRuleDO> findByOrgCodeAndEnableStatus(String orgCode, Integer enableStatus, Pageable pageable) {
        return emailSendRuleMapper.findByOrgCodeAndEnableStatus(orgCode, enableStatus.shortValue(), pageable)
                .map(emailSendRuleConverter::toDO);
    }

    @Override
    public long countByOrgCode(String orgCode) {
        return emailSendRuleMapper.countByOrgCode(orgCode);
    }

    @Override
    public List<EmailSendRuleDO> findExecutableRules() {
        return emailSendRuleMapper.findExecutableRules(LocalDateTime.now())
                .stream()
                .map(emailSendRuleConverter::toDO)
                .collect(Collectors.toList());
    }

    @Override
    public void updateExecutionInfo(Long id, Date lastExecuteTime, Date nextExecuteTime, int executedCount) {
        emailSendRuleMapper.updateExecutionInfo(id, toLocalDateTime(lastExecuteTime), toLocalDateTime(nextExecuteTime), executedCount);
    }
    
    @Override
    public void update(Long id, EmailSendRuleDO rule) {
        var po = emailSendRuleConverter.toPO(rule);
        po.setId(id);
        emailSendRuleMapper.save(po);
    }

    @Override
    public Page<EmailSendRuleDO> findByOrgId(String orgId, Pageable pageable) {
        return emailSendRuleMapper.findByOrgCode(orgId, pageable)
                .map(emailSendRuleConverter::toDO);
    }

    @Override
    public void deleteByOrgCodeAndRuleId(String orgCode, String ruleId) {
        emailSendRuleMapper.deleteByOrgCodeAndRuleId(orgCode, ruleId);
    }

    @Override
    public void deleteByOrgCode(String orgCode) {
        emailSendRuleMapper.deleteByOrgCode(orgCode);
    }

    @Override
    public EmailSendRuleDO findByOrgCodeAndRuleName(String orgCode, String ruleName) {
        return emailSendRuleMapper.findByOrgCodeAndRuleName(orgCode, ruleName)
                .map(emailSendRuleConverter::toDO)
                .orElse(null);
    }

    @Override
    public EmailSendRuleDO findByRuleId(String ruleId) {
        return emailSendRuleMapper.findByRuleId(ruleId)
                .map(emailSendRuleConverter::toDO)
                .orElse(null);
    }

    @Override
    public void deleteByRuleId(String ruleId) {
        emailSendRuleMapper.softDeleteByRuleId(ruleId);
    }

    @Override
    public List<EmailSendRuleDO> findRulesForExecution(Date now) {
        java.time.LocalDateTime ldt = now.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime();
        return emailSendRuleMapper.findExecutableRules(ldt)
                .stream()
                .map(emailSendRuleConverter::toDO)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public Page<EmailSendRuleDO> pageQuery(EmailSendRulePageQueryDTO query) {
        int pageIndex = query.getPageNum() > 0 ? query.getPageNum() - 1 : 0;
        Pageable pageable = PageRequest.of(pageIndex, query.getPageSize());
        Page<EmailSendRuleDO> page = emailSendRuleMapper.findByOrgCode(query.getOrgCode(), pageable)
                .map(emailSendRuleConverter::toDO);
        if (query.getRuleName() == null) {
            return page;
        }
        java.util.List<EmailSendRuleDO> filtered = page.getContent().stream()
                .filter(r -> r.getRuleName()!=null && r.getRuleName().contains(query.getRuleName()))
                .toList();
        return new org.springframework.data.domain.PageImpl<>(filtered, pageable, filtered.size());
    }

    private LocalDateTime toLocalDateTime(Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
} 