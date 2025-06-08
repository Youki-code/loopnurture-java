package org.springframework.samples.loopnurture.mail.infra.mapper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.loopnurture.mail.infra.po.EmailRulePO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 邮件规则JPA Mapper
 */
public interface JpaEmailRuleMapper extends JpaRepository<EmailRulePO, String> {
    
    /**
     * 根据组织ID和规则名称查询
     */
    Optional<EmailRulePO> findByOrgIdAndRuleName(String orgId, String ruleName);

    /**
     * 查询需要执行的规则
     */
    @Query("SELECT r FROM EmailRulePO r WHERE r.isActive = true " +
           "AND (r.nextExecutionTime IS NULL OR r.nextExecutionTime <= :now)")
    List<EmailRulePO> findRulesForExecution(@Param("now") LocalDateTime now);

    /**
     * 查询组织的规则
     */
    Page<EmailRulePO> findByOrgId(String orgId, Pageable pageable);

    /**
     * 检查规则名是否存在
     */
    boolean existsByOrgIdAndRuleName(String orgId, String ruleName);

    /**
     * 更新执行信息
     */
    @Modifying
    @Query("UPDATE EmailRulePO r SET r.lastExecutionTime = :lastTime, " +
           "r.nextExecutionTime = :nextTime, r.executionCount = :count " +
           "WHERE r.id = :id")
    void updateExecutionInfo(@Param("id") String id,
                           @Param("lastTime") LocalDateTime lastExecutionTime,
                           @Param("nextTime") LocalDateTime nextExecutionTime,
                           @Param("count") int executionCount);
} 