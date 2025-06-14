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
     * 根据组织ID查找规则
     */
    Page<EmailSendRuleDO> findByOrgId(String orgId, Pageable pageable);


    /**
     * 更新规则执行信息
     */
    void updateExecutionInfo(String id, Date lastExecuteTime, Date nextExecuteTime, int executedCount);

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
     * 查找需要执行的规则
     */
    List<EmailSendRuleDO> findRulesForExecution(Date now);

    /**
     * 统计组织的规则数量
     */
    long countByOrgCode(String orgCode);

    void update(String id, EmailSendRuleDO rule);


    Page<EmailSendRuleDO> pageQuery(EmailSendRulePageQueryDTO query);

    /**
     * 根据组织编码和规则名称查询规则
     */
    EmailSendRuleDO findByOrgCodeAndRuleName(String orgCode, String ruleName);

    /**
     * 根据 ruleId 逻辑删除（deleted = true）
     */
    void deleteByRuleId(String ruleId);



    /**
     * 根据业务 ruleId 查询规则（跨组织唯一）
     */
    EmailSendRuleDO findByRuleId(String ruleId);

    /**
     * 查询所有满足执行条件的规则（启用、到达执行时间且未超最大次数）
     */
    List<EmailSendRuleDO> findExecutableRules(Date now);
} 