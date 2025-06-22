package org.springframework.samples.loopnurture.mail.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.samples.loopnurture.mail.domain.model.EmailSendRuleDO;
import org.springframework.samples.loopnurture.mail.domain.repository.dto.EmailSendRulePageQueryDTO;

import java.util.Date;
import java.util.List;

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
    EmailSendRuleDO findById(Long id);

    /**
     * 根据组织编码和规则ID查找规则
     */
    EmailSendRuleDO findByOrgCodeAndRuleId(String orgCode, String ruleId);

    /**
     * 分页查询组织的所有规则
     */
    Page<EmailSendRuleDO> findByOrgCode(String orgCode, Pageable pageable);

    /**
     * 根据组织编码和模板ID分页查询规则
     */
    Page<EmailSendRuleDO> findByOrgCodeAndTemplateId(String orgCode, String templateId, Pageable pageable);

    /**
     * 根据组织编码和规则类型分页查询规则
     */
    Page<EmailSendRuleDO> findByOrgCodeAndRuleType(String orgCode, Integer ruleType, Pageable pageable);

    /**
     * 根据组织编码和启用状态分页查询规则
     */
    Page<EmailSendRuleDO> findByOrgCodeAndEnableStatus(String orgCode, Integer enableStatus, Pageable pageable);

    /**
     * 查找需要执行的规则
     */
    List<EmailSendRuleDO> findExecutableRules();

    /**
     * 统计组织的规则数量
     */
    long countByOrgCode(String orgCode);

    /**
     * 更新规则执行信息
     */
    void updateExecutionInfo(Long id, Date lastExecuteTime, Date nextExecuteTime, int executedCount);

    /**
     * 更新规则
     */
    void update(Long id, EmailSendRuleDO rule);

    /**
     * 根据组织ID查找规则
     */
    Page<EmailSendRuleDO> findByOrgId(String orgId, Pageable pageable);

    /**
     * 根据组织编码和规则ID删除规则
     */
    void deleteByOrgCodeAndRuleId(String orgCode, String ruleId);

    /**
     * 根据组织编码删除所有规则
     */
    void deleteByOrgCode(String orgCode);

    /**
     * 根据组织编码和规则名称查询
     */
    EmailSendRuleDO findByOrgCodeAndRuleName(String orgCode, String ruleName);

    /**
     * 根据业务 ruleId 查询规则
     */
    EmailSendRuleDO findByRuleId(String ruleId);

    /**
     * 按 ruleId 逻辑删除
     */
    void deleteByRuleId(String ruleId);

    /**
     * 查找需要执行的规则
     */
    List<EmailSendRuleDO> findRulesForExecution(Date now);

    /**
     * 根据查询条件分页查询规则
     */
    Page<EmailSendRuleDO> pageQuery(EmailSendRulePageQueryDTO query);
} 