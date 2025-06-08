package org.springframework.samples.loopnurture.mail.infra.mapper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.samples.loopnurture.mail.infra.po.MarketingEmailTemplatePO;

import java.util.List;
import java.util.Optional;

/**
 * 营销邮件模板JPA Mapper接口
 */
public interface JpaMarketingEmailTemplateMapper extends 
        JpaRepository<MarketingEmailTemplatePO, Long>,
        JpaSpecificationExecutor<MarketingEmailTemplatePO> {
    
    /**
     * 根据组织ID和模板代码查找模板
     */
    Optional<MarketingEmailTemplatePO> findByOrgIdAndTemplateCode(Long orgId, String templateCode);

    /**
     * 查找组织的所有启用模板
     */
    @Query("SELECT t FROM MarketingEmailTemplatePO t WHERE t.orgId = ?1 AND t.status = 2")
    List<MarketingEmailTemplatePO> findActiveTemplatesByOrgId(Long orgId);

    /**
     * 分页查询组织的所有模板
     */
    Page<MarketingEmailTemplatePO> findByOrgId(Long orgId, Pageable pageable);

    /**
     * 检查模板代码是否已存在
     */
    boolean existsByOrgIdAndTemplateCode(Long orgId, String templateCode);

    /**
     * 根据组织ID和状态查询模板
     */
    Page<MarketingEmailTemplatePO> findByOrgIdAndStatus(Long orgId, Integer status, Pageable pageable);

    /**
     * 根据组织ID和状态查询模板列表
     */
    List<MarketingEmailTemplatePO> findByOrgIdAndStatus(Long orgId, Integer status);

    /**
     * 统计组织的模板数量
     */
    long countByOrgIdAndStatus(Long orgId, Integer status);
} 