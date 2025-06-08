package org.springframework.samples.loopnurture.mail.infra.mapper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.loopnurture.mail.infra.po.EmailSendRulePO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 邮件发送规则JPA Mapper接口
 */
public interface JpaEmailSendRuleMapper extends 
        JpaRepository<EmailSendRulePO, Long>,
        JpaSpecificationExecutor<EmailSendRulePO> {
    
    /**
     * 根据组织ID和规则名称查找规则
     */
    Optional<EmailSendRulePO> findByOrgIdAndRuleName(Long orgId, String ruleName);

    /**
     * 查找需要执行的规则
     */
    @Query("SELECT r FROM EmailSendRulePO r WHERE r.enabled = true AND r.nextExecutionTime <= :now")
    List<EmailSendRulePO> findRulesForExecution(@Param("now") LocalDateTime now);

    /**
     * 分页查询组织的所有规则
     */
    Page<EmailSendRulePO> findByOrgId(Long orgId, Pageable pageable);

    /**
     * 检查规则名称是否已存在
     */
    boolean existsByOrgIdAndRuleName(Long orgId, String ruleName);

    /**
     * 更新规则执行信息
     */
    @Modifying
    @Query("UPDATE EmailSendRulePO r SET r.lastExecutionTime = :lastExecutionTime, " +
           "r.nextExecutionTime = :nextExecutionTime, r.executionCount = :executionCount " +
           "WHERE r.id = :id")
    void updateExecutionInfo(@Param("id") Long id,
                           @Param("lastExecutionTime") LocalDateTime lastExecutionTime,
                           @Param("nextExecutionTime") LocalDateTime nextExecutionTime,
                           @Param("executionCount") int executionCount);
} 