package org.springframework.samples.loopnurture.mail.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.samples.loopnurture.mail.domain.model.EmailSendRuleDO;
import org.springframework.samples.loopnurture.mail.domain.enums.RuleTypeEnum;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 邮件发送规则仓储接口
 */
public interface EmailSendRuleRepository {
    /**
     * 保存规则
     */
    EmailSendRuleDO save(EmailSendRuleDO rule);

    /**
     * 根据ID查找规则
     */
    EmailSendRuleDO findById(String id);

    /**
     * 根据ID删除规则
     */
    void deleteById(String id);

    /**
     * 根据组织ID查找规则
     */
    Page<EmailSendRuleDO> findByOrgId(String orgId, Pageable pageable);

    /**
     * 检查规则ID是否存在
     */
    boolean existsById(String id);

    /**
     * 检查组织下是否存在同名规则
     */
    boolean existsByOrgIdAndRuleName(String orgId, String ruleName);

    /**
     * 查找所有规则
     */
    List<EmailSendRuleDO> findAll();

    /**
     * 更新规则执行信息
     */
    void updateExecutionInfo(String id, LocalDateTime lastExecuteTime, LocalDateTime nextExecuteTime, int executedCount);

    /**
     * 根据组织编码和规则ID查找规则
     */
    EmailSendRuleDO findByOrgCodeAndRuleId(String orgCode, String ruleId);

    /**
     * 查找组织的所有启用规则
     */
    List<EmailSendRuleDO> findActiveRulesByOrgCode(String orgCode);

    /**
     * 分页查询组织的所有规则
     */
    Page<EmailSendRuleDO> findByOrgCode(String orgCode, Pageable pageable);

    /**
     * 检查规则ID是否已存在
     */
    boolean existsByOrgCodeAndRuleId(String orgCode, String ruleId);

    /**
     * 查找需要执行的规则
     */
    List<EmailSendRuleDO> findRulesForExecution(LocalDateTime now);

    /**
     * 统计组织的规则数量
     */
    long countByOrgCode(String orgCode);

    void update(String id, EmailSendRuleDO rule);

    boolean existsByOrgCodeAndRuleName(String orgCode, String ruleName);
} 