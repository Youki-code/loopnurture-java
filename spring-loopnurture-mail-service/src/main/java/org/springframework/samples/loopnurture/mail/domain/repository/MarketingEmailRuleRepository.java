package org.springframework.samples.loopnurture.mail.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.loopnurture.mail.domain.model.MarketingEmailRuleDO;
import java.util.List;

/**
 * 营销邮件规则仓储接口
 */
public interface MarketingEmailRuleRepository extends JpaRepository<MarketingEmailRuleDO, String> {
    /**
     * 根据组织ID查询规则列表
     */
    List<MarketingEmailRuleDO> findByOrgId(String orgId);

    /**
     * 根据模板ID查询规则列表
     */
    List<MarketingEmailRuleDO> findByTemplateId(String templateId);

    /**
     * 根据状态查询规则列表
     */
    List<MarketingEmailRuleDO> findByOrgIdAndStatus(String orgId, Integer status);

    /**
     * 更新规则状态
     */
    @Modifying
    @Query("UPDATE MarketingEmailRuleDO r SET r.status = :status WHERE r.ruleId = :ruleId")
    void updateStatus(@Param("ruleId") String ruleId, @Param("status") Integer status);
} 