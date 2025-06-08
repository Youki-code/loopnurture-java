package org.springframework.samples.loopnurture.mail.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.samples.loopnurture.mail.domain.model.EmailRuleDO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 邮件规则领域仓储接口
 */
public interface EmailRuleRepository {
    
    /**
     * 保存邮件规则
     */
    EmailRuleDO save(EmailRuleDO rule);

    /**
     * 根据ID查找邮件规则
     */
    Optional<EmailRuleDO> findById(String id);

    /**
     * 根据组织ID和规则名称查找规则
     */
    Optional<EmailRuleDO> findByOrgIdAndRuleName(String orgId, String ruleName);

    /**
     * 查找需要执行的规则
     */
    List<EmailRuleDO> findRulesForExecution(LocalDateTime now);

    /**
     * 分页查询组织的所有规则
     */
    Page<EmailRuleDO> findByOrgId(String orgId, Pageable pageable);

    /**
     * 检查规则名称是否已存在
     */
    boolean existsByOrgIdAndRuleName(String orgId, String ruleName);

    /**
     * 删除邮件规则
     */
    void deleteById(String id);

    /**
     * 更新规则执行信息
     */
    void updateExecutionInfo(String id, LocalDateTime lastExecutionTime, 
                           LocalDateTime nextExecutionTime, int executionCount);
} 