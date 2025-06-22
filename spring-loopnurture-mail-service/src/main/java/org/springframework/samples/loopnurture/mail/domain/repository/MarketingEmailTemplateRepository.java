package org.springframework.samples.loopnurture.mail.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.samples.loopnurture.mail.domain.model.MarketingEmailTemplateDO;
import org.springframework.samples.loopnurture.mail.domain.repository.dto.MarketingEmailTemplatePageQueryDTO;

import java.util.List;
import java.util.Optional;

/**
 * 营销邮件模板仓储接口
 */
public interface MarketingEmailTemplateRepository {
    
    /**
     * 保存模板
     */
    MarketingEmailTemplateDO save(MarketingEmailTemplateDO template);
    
    /**
     * 根据ID查找模板
     */
    MarketingEmailTemplateDO findById(Long id);
    
    /**
     * 根据组织编码和模板ID查找模板
     */
    Optional<MarketingEmailTemplateDO> findByOrgCodeAndTemplateId(String orgCode, String templateId);
    
    /**
     * 根据组织编码分页查询模板
     */
    Page<MarketingEmailTemplateDO> findByOrgCode(String orgCode, Pageable pageable);
    
    /**
     * 根据组织编码和启用状态分页查询模板
     */
    Page<MarketingEmailTemplateDO> findByOrgCodeAndEnableStatus(String orgCode, Integer enableStatus, Pageable pageable);
    
    /**
     * 根据组织编码和内容类型分页查询模板
     */
    Page<MarketingEmailTemplateDO> findByOrgCodeAndContentType(String orgCode, Integer contentType, Pageable pageable);
    
    /**
     * 统计组织的模板数量
     */
    long countByOrgCode(String orgCode);
    
    /**
     * 根据组织编码和启用状态统计模板数量
     */
    long countByOrgCodeAndEnableStatus(String orgCode, Integer enableStatus);
    
    /**
     * 根据组织编码和模板ID删除模板
     */
    void deleteByOrgCodeAndTemplateId(String orgCode, String templateId);
    
    /**
     * 根据组织编码删除所有模板
     */
    void deleteByOrgCode(String orgCode);
    
    /**
     * 检查模板ID是否已存在
     */
    boolean existsByOrgCodeAndTemplateId(String orgCode, String templateId);
    
    /**
     * 根据组织编码和模板名称模糊查询
     */
    Page<MarketingEmailTemplateDO> findByOrgCodeAndTemplateNameContaining(String orgCode, String templateName, Pageable pageable);

    /**
     * 根据业务 templateId 获取模板
     */
    MarketingEmailTemplateDO getByTemplateId(String templateId);

    /**
     * 精确匹配组织+模板名
     */
    List<MarketingEmailTemplateDO> findByOrgCodeAndTemplateName(String orgCode, String templateName);

    /**
     * 逻辑删除
     */
    void deleteByTemplateId(String templateId);

    /**
     * 分页条件查询
     */
    Page<MarketingEmailTemplateDO> pageQuery(MarketingEmailTemplatePageQueryDTO query);

    /**
     * Specification 支持
     */
    Page<MarketingEmailTemplateDO> findAll(Specification<MarketingEmailTemplateDO> spec, Pageable pageable);
} 