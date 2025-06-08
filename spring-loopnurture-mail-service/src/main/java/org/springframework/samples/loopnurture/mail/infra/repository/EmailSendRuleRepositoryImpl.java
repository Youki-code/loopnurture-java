package org.springframework.samples.loopnurture.mail.infra.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.samples.loopnurture.mail.domain.model.EmailSendRuleDO;
import org.springframework.samples.loopnurture.mail.domain.repository.EmailSendRuleRepository;
import org.springframework.samples.loopnurture.mail.infra.converter.EmailSendRuleConverter;
import org.springframework.samples.loopnurture.mail.infra.mapper.JpaEmailSendRuleMapper;
import org.springframework.samples.loopnurture.mail.infra.po.EmailSendRulePO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 邮件发送规则仓储实现类
 */
@Repository
@RequiredArgsConstructor
public class EmailSendRuleRepositoryImpl implements EmailSendRuleRepository {

    private final JpaEmailSendRuleMapper jpaMapper;
    private final EmailSendRuleConverter converter;

    @Override
    public EmailSendRuleDO save(EmailSendRuleDO rule) {
        var po = converter.toPO(rule);
        po = jpaMapper.save(po);
        return converter.toDO(po);
    }

    @Override
    public Optional<EmailSendRuleDO> findById(Long id) {
        return jpaMapper.findById(id)
            .map(converter::toDO);
    }

    @Override
    public Optional<EmailSendRuleDO> findByOrgIdAndRuleName(Long orgId, String ruleName) {
        return jpaMapper.findByOrgIdAndRuleName(orgId, ruleName)
            .map(converter::toDO);
    }

    @Override
    public List<EmailSendRuleDO> findRulesForExecution(LocalDateTime now) {
        return jpaMapper.findRulesForExecution(now).stream()
            .map(converter::toDO)
            .collect(Collectors.toList());
    }

    @Override
    public Page<EmailSendRuleDO> findByOrgId(Long orgId, Pageable pageable) {
        return jpaMapper.findByOrgId(orgId, pageable)
            .map(converter::toDO);
    }

    @Override
    public boolean existsByOrgIdAndRuleName(Long orgId, String ruleName) {
        return jpaMapper.existsByOrgIdAndRuleName(orgId, ruleName);
    }

    @Override
    public void deleteById(Long id) {
        jpaMapper.deleteById(id);
    }

    @Override
    @Transactional
    public void updateExecutionInfo(Long id, LocalDateTime lastExecutionTime, 
                                  LocalDateTime nextExecutionTime, int executionCount) {
        jpaMapper.updateExecutionInfo(id, lastExecutionTime, nextExecutionTime, executionCount);
    }
} 