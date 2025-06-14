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
        var po = emailSendRuleConverter.toPO(rule);
        emailSendRuleMapper.save(po);
        return emailSendRuleConverter.toDO(po);
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
    public void deleteByRuleId(String ruleId) {
        emailSendRuleMapper.softDeleteByRuleId(ruleId);
    }

    @Override
    public List<EmailSendRuleDO> findRulesForExecution(Date now) {
        LocalDateTime ldt = toLocalDateTime(now);
        return emailSendRuleMapper.findByIsActiveTrueAndNextExecutionTimeLessThanEqual(ldt)
                .stream()
                .map(emailSendRuleConverter::toDO)
                .collect(Collectors.toList());
    }

    @Override
    public long countByOrgCode(String orgCode) {
        return emailSendRuleMapper.countByOrgCode(orgCode);
    }

    @Override
    public void updateExecutionInfo(String id, Date lastExecuteTime, Date nextExecuteTime, int executedCount) {
        emailSendRuleMapper.updateExecutionInfo(id, toLocalDateTime(lastExecuteTime), toLocalDateTime(nextExecuteTime), executedCount);
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

    @Override
    public Page<EmailSendRuleDO> pageQuery(EmailSendRulePageQueryDTO query){
        Pageable pageable = PageRequest.of(query.getPageNum(), query.getPageSize());

        Specification<EmailSendRulePO> spec = (root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (query.getEnableStatusList() != null && !query.getEnableStatusList().isEmpty()) {
                predicates.add(root.get("enableStatus").in(query.getEnableStatusList()));
            }

            if (query.getRuleName() != null && !query.getRuleName().isBlank()) {
                predicates.add(cb.like(root.get("ruleName"), "%" + query.getRuleName() + "%"));
            }

            if (query.getTemplateId() != null && !query.getTemplateId().isBlank()) {
                predicates.add(cb.equal(root.get("templateId"), query.getTemplateId()));
            }

            if (query.getRuleType() != null) {
                predicates.add(cb.equal(root.get("ruleType"), query.getRuleType()));
            }

            if (query.getOrgCode() != null && !query.getOrgCode().isBlank()) {
                predicates.add(cb.equal(root.get("orgCode"), query.getOrgCode()));
            }

            // recipientType 暂无对应列，可在此扩展

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return emailSendRuleMapper.findAll(spec, pageable).map(emailSendRuleConverter::toDO);
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
    public List<EmailSendRuleDO> findExecutableRules(Date now) {
        LocalDateTime ldt = toLocalDateTime(now);
        Specification<EmailSendRulePO> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("enableStatus"), EnableStatusEnum.ENABLED.getCode()));
            predicates.add(cb.lessThanOrEqualTo(root.get("nextExecutionTime"), ldt));
            predicates.add(
                cb.or(
                    cb.isNull(root.get("maxExecutions")),
                    cb.lessThan(root.get("executionCount"), root.get("maxExecutions"))
                )
            );
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return emailSendRuleMapper.findAll(spec).stream()
                .map(emailSendRuleConverter::toDO)
                .collect(Collectors.toList());
    }

    private LocalDateTime toLocalDateTime(Date date) {
        return date == null ? null : date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
} 