package org.springframework.samples.loopnurture.mail.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.loopnurture.mail.domain.model.MarketingEmailTemplateDO;
import java.util.List;

/**
 * 营销邮件模板仓储接口
 */
public interface MarketingEmailTemplateRepository extends JpaRepository<MarketingEmailTemplateDO, String> {
    /**
     * 根据组织ID查询模板列表
     */
    List<MarketingEmailTemplateDO> findByOrgId(String orgId);

    /**
     * 根据状态查询模板列表
     */
    List<MarketingEmailTemplateDO> findByOrgIdAndStatus(String orgId, Integer status);

    /**
     * 更新模板状态
     */
    @Modifying
    @Query("UPDATE MarketingEmailTemplateDO t SET t.status = :status WHERE t.templateId = :templateId")
    void updateStatus(@Param("templateId") String templateId, @Param("status") Integer status);
} 