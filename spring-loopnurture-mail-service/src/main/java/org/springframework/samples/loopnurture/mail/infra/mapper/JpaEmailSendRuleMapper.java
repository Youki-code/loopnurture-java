package org.springframework.samples.loopnurture.mail.infra.mapper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.loopnurture.mail.infra.po.EmailSendRulePO;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 邮件发送规则JPA Mapper接口
 */
@Repository
public interface JpaEmailSendRuleMapper extends JpaRepository<EmailSendRulePO, Long>, JpaSpecificationExecutor<EmailSendRulePO> {
    
    /**
     * 根据组织编码查找规则
     */
    List<EmailSendRulePO> findByOrgCode(String orgCode);

    /**
     * 分页查询组织的所有规则
     */
    Page<EmailSendRulePO> findByOrgCode(String orgCode, Pageable pageable);

    /**
     * 根据组织编码和规则名称查找规则
     */
    Optional<EmailSendRulePO> findByOrgCodeAndRuleName(String orgCode, String ruleName);

    /**
     * 根据组织编码和规则ID查找规则
     */
    Optional<EmailSendRulePO> findByOrgCodeAndRuleId(String orgCode, String ruleId);

    /**
     * 根据组织编码和启用状态分页查询规则
     */
    Page<EmailSendRulePO> findByOrgCodeAndEnableStatus(String orgCode, Integer enableStatus, Pageable pageable);
    
    /**
     * 根据组织编码和启用状态查找规则
     */
    List<EmailSendRulePO> findByOrgCodeAndEnableStatus(String orgCode, Integer enableStatus);

    /**
     * 根据组织编码统计规则数量
     */
    long countByOrgCode(String orgCode);

    /**
     * 检查组织编码和规则名称是否已存在
     */
    boolean existsByOrgCodeAndRuleName(String orgCode, String ruleName);

    /**
     * 检查组织编码和规则ID是否已存在
     */
    boolean existsByOrgCodeAndRuleId(String orgCode, String ruleId);

    /**
     * 根据业务 ruleId 查询规则
     */
    Optional<EmailSendRulePO> findByRuleId(String ruleId);

    /**
     * 查找需要执行的规则
     */
    @Query("SELECT e FROM EmailSendRulePO e WHERE e.enableStatus = 1 " +
            "AND (e.startTime IS NULL OR e.startTime <= :now) " +
            "AND (e.endTime IS NULL OR e.endTime >= :now) " +
            "AND (e.maxExecutions IS NULL OR e.executionCount < e.maxExecutions) " +
            "AND (e.nextExecutionTime IS NULL OR e.nextExecutionTime <= :now)")
    List<EmailSendRulePO> findRulesForExecution(@Param("now") LocalDateTime now);

    /**
     * 查询启用状态=1 且 nextExecutionTime<=now 的规则
     */
    @Query("SELECT e FROM EmailSendRulePO e WHERE e.enableStatus = 1 AND e.nextExecutionTime <= :now")
    List<EmailSendRulePO> findEnabledRulesDue(@Param("now") LocalDateTime now);

    /**
     * 根据组织编码、启用状态和当前时间查找需要执行的规则
     */
    @Query("SELECT e FROM EmailSendRulePO e WHERE e.orgCode = :orgCode " +
           "AND e.enableStatus = :enableStatus AND e.nextExecutionTime <= :currentTime")
    List<EmailSendRulePO> findRulesForExecution(@Param("orgCode") String orgCode,
                                               @Param("enableStatus") Integer enableStatus,
                                               @Param("currentTime") LocalDateTime currentTime);

    /**
     * 更新规则执行信息（根据ID）
     */
    @Modifying
    @Query("UPDATE EmailSendRulePO e SET e.lastExecutionTime = :lastExecutionTime, " +
            "e.nextExecutionTime = :nextExecutionTime, e.executionCount = :executionCount " +
            "WHERE e.id = :id")
    void updateExecutionInfo(@Param("id") Long id,
                             @Param("lastExecutionTime") LocalDateTime lastExecutionTime,
                             @Param("nextExecutionTime") LocalDateTime nextExecutionTime,
                             @Param("executionCount") int executionCount);

    /**
     * 更新规则执行信息（根据ruleId）
     */
    @Modifying
    @Query("UPDATE EmailSendRulePO e SET e.lastExecutionTime = :lastExecutionTime, " +
           "e.nextExecutionTime = :nextExecutionTime, e.executionCount = e.executionCount + 1 " +
           "WHERE e.ruleId = :ruleId")
    void updateExecutionInfo(@Param("ruleId") String ruleId,
                           @Param("lastExecutionTime") LocalDateTime lastExecutionTime,
                           @Param("nextExecutionTime") LocalDateTime nextExecutionTime);

    /**
     * 逻辑删除规则（根据ID）
     */
    @Modifying
    @Query("UPDATE EmailSendRulePO e SET e.deleted = true WHERE e.id = :id")
    void softDeleteById(@Param("id") Long id);

    /**
     * 根据 ruleId 逻辑删除
     */
    @Modifying
    @Query("UPDATE EmailSendRulePO e SET e.deleted = true WHERE e.ruleId = :ruleId")
    void softDeleteByRuleId(@Param("ruleId") String ruleId);

    /**
     * 根据组织编码和模板ID查询邮件发送规则
     */
    Page<EmailSendRulePO> findByOrgCodeAndTemplateId(String orgCode, String templateId, Pageable pageable);

    /**
     * 根据组织编码和规则类型查询邮件发送规则
     */
    Page<EmailSendRulePO> findByOrgCodeAndRuleType(String orgCode, Short ruleType, Pageable pageable);

    /**
     * 根据组织编码和启用状态查询邮件发送规则
     */
    Page<EmailSendRulePO> findByOrgCodeAndEnableStatus(String orgCode, Short enableStatus, Pageable pageable);

    /**
     * 根据组织编码和启用状态统计规则数量
     */
    long countByOrgCodeAndEnableStatus(String orgCode, Short enableStatus);

    /**
     * 查找需要执行的规则,startTime小于等于currentTime，endTime大于currentTime,并且已经执行次数小于最大执行次数，且当前输入时间小于nextExecutionTime 
     */
    @Query("SELECT e FROM EmailSendRulePO e WHERE e.nextExecutionTime <= :currentTime AND e.startTime <= :currentTime AND e.endTime >= :currentTime AND e.executionCount < e.maxExecutions AND e.enableStatus = 1 AND e.deleted = false")
    List<EmailSendRulePO> findExecutableRules(@Param("currentTime") LocalDateTime currentTime);

    /**
     * 根据组织编码和规则ID删除规则
     */
    void deleteByOrgCodeAndRuleId(String orgCode, String ruleId);

    /**
     * 根据组织编码删除所有规则
     */
    void deleteByOrgCode(String orgCode);
} 