package org.springframework.samples.loopnurture.mail.infra.mapper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.loopnurture.mail.infra.po.MarketingEmailTemplatePO;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 营销邮件模板JPA Mapper接口
 */
@Repository
public interface JpaMarketingEmailTemplateMapper extends JpaRepository<MarketingEmailTemplatePO, Long>, JpaSpecificationExecutor<MarketingEmailTemplatePO> {
    
    /**
     * 根据组织编码查询邮件模板
     */
    Page<MarketingEmailTemplatePO> findByOrgCode(String orgCode, Pageable pageable);
    
    /**
     * 根据组织编码和模板ID查询邮件模板
     */
    Optional<MarketingEmailTemplatePO> findByOrgCodeAndTemplateId(String orgCode, String templateId);
    
    /**
     * 根据组织编码和启用状态查询邮件模板
     */
    Page<MarketingEmailTemplatePO> findByOrgCodeAndEnableStatus(String orgCode, Short enableStatus, Pageable pageable);
    
    /**
     * 根据组织编码和内容类型查询邮件模板
     */
    Page<MarketingEmailTemplatePO> findByOrgCodeAndContentType(String orgCode, Short contentType, Pageable pageable);
    
    /**
     * 根据组织编码统计模板数量
     */
    long countByOrgCode(String orgCode);
    
    /**
     * 根据组织编码和启用状态统计模板数量
     */
    long countByOrgCodeAndEnableStatus(String orgCode, Short enableStatus);
    
    /**
     * 根据组织编码和模板ID删除模板
     */
    @Deprecated
    void deleteByOrgCodeAndTemplateId(String orgCode, String templateId);
    
    /**
     * 根据组织编码删除所有模板
     */
    @Deprecated
    void deleteByOrgCode(String orgCode);
    
    /**
     * 检查模板ID是否已存在
     */
    boolean existsByOrgCodeAndTemplateId(String orgCode, String templateId);
    
    /**
     * 根据组织编码和模板名称模糊查询
     */
    @Query("SELECT t FROM MarketingEmailTemplatePO t WHERE t.orgCode = :orgCode AND t.templateName LIKE %:templateName% AND t.deleted = false")
    Page<MarketingEmailTemplatePO> findByOrgCodeAndTemplateNameContaining(@Param("orgCode") String orgCode, 
                                                                         @Param("templateName") String templateName, 
                                                                         Pageable pageable);

    /**
     * 逻辑删除（单条）
     */
    @org.springframework.transaction.annotation.Transactional
    @org.springframework.data.jpa.repository.Modifying
    @org.springframework.data.jpa.repository.Query("UPDATE MarketingEmailTemplatePO t SET t.deleted = true WHERE t.orgCode = :orgCode AND t.templateId = :templateId")
    void softDeleteByOrgCodeAndTemplateId(@Param("orgCode") String orgCode, @Param("templateId") String templateId);

    /**
     * 逻辑删除组织所有模板
     */
    @org.springframework.transaction.annotation.Transactional
    @org.springframework.data.jpa.repository.Modifying
    @org.springframework.data.jpa.repository.Query("UPDATE MarketingEmailTemplatePO t SET t.deleted = true WHERE t.orgCode = :orgCode")
    void softDeleteByOrgCode(@Param("orgCode") String orgCode);

    /**
     * 根据模板ID查询邮件模板（不区分组织）
     */
    Optional<MarketingEmailTemplatePO> findByTemplateId(String templateId);
} 