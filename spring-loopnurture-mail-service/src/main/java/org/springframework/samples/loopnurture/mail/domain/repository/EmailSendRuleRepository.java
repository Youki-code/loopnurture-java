package org.springframework.samples.loopnurture.mail.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.samples.loopnurture.mail.domain.model.EmailSendRuleDO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 邮件发送规则领域仓储接口
 */
public interface EmailSendRuleRepository {
    
    /**
     * 保存邮件发送规则
     */
    EmailSendRuleDO save(EmailSendRuleDO rule);

    /**
     * 根据ID查找邮件发送规则
     */
    Optional<EmailSendRuleDO> findById(Long id);

    /**
     * 根据组织ID和规则名称查找规则
     */
    Optional<EmailSendRuleDO> findByOrgIdAndRuleName(Long orgId, String ruleName);

    /**
     * 查找需要执行的规则
     */
    List<EmailSendRuleDO> findRulesForExecution(LocalDateTime now);

    /**
     * 分页查询组织的所有规则
     */
    Page<EmailSendRuleDO> findByOrgId(Long orgId, Pageable pageable);

    /**
     * 检查规则名称是否已存在
     */
    boolean existsByOrgIdAndRuleName(Long orgId, String ruleName);

    /**
     * 删除邮件发送规则
     */
    void deleteById(Long id);

    /**
     * 更新规则执行信息
     */
    void updateExecutionInfo(Long id, LocalDateTime lastExecutionTime, 
                           LocalDateTime nextExecutionTime, int executionCount);
} 