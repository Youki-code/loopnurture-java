package org.springframework.samples.loopnurture.mail.infra.mapper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.loopnurture.mail.infra.po.MarketingEmailTemplatePO;

import java.util.List;
import java.util.Optional;

/**
 * 营销邮件模板JPA Mapper接口
 */
public interface JpaMarketingEmailTemplateMapper extends JpaRepository<MarketingEmailTemplatePO, String>, 
        JpaSpecificationExecutor<MarketingEmailTemplatePO> {
    
    Optional<MarketingEmailTemplatePO> findByOrgCodeAndTemplateId(String orgCode, String templateId);
    
    Optional<MarketingEmailTemplatePO> findByTemplateId(String templateId);
    
    List<MarketingEmailTemplatePO> findByOrgCodeAndEnableStatus(String orgCode, Integer enableStatus);
    
    Page<MarketingEmailTemplatePO> findByOrgCode(String orgCode, Pageable pageable);
    
    Page<MarketingEmailTemplatePO> findByOrgCodeAndEnableStatus(String orgCode, Integer enableStatus, Pageable pageable);
    
    long countByOrgCodeAndEnableStatus(String orgCode, Integer enableStatus);

    /**
     * 根据模板代码查找模板
     */
    Optional<MarketingEmailTemplatePO> findByTemplateCode(String templateCode);

    /**
     * 根据组织编码和模板代码查找模板
     */
    Optional<MarketingEmailTemplatePO> findByOrgCodeAndTemplateCode(String orgCode, String templateCode);

    /**
     * 检查模板代码是否已存在
     */
    boolean existsByTemplateCode(String templateCode);

    /**
     * 统计组织的模板数量
     */
    long countByOrgCode(String orgCode);

    @org.springframework.data.jpa.repository.Modifying
    @org.springframework.data.jpa.repository.Query("update MarketingEmailTemplatePO set deleted = true where template_id = :templateId")
    void softDeleteByTemplateId(@Param("templateId") String templateId);

    /**
     * 根据组织编码和模板名称查询模板
     */
    List<MarketingEmailTemplatePO> findByOrgCodeAndTemplateName(String orgCode, String templateName);
} 