package org.springframework.samples.loopnurture.mail.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.samples.loopnurture.mail.domain.model.MarketingEmailTemplateDO;
import org.springframework.samples.loopnurture.mail.domain.enums.EnableStatusEnum;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import jakarta.persistence.criteria.Predicate;

/**
 * 营销邮件模板领域仓储接口
 */
public interface MarketingEmailTemplateRepository {
    
    /**
     * 保存营销邮件模板
     */
    MarketingEmailTemplateDO save(MarketingEmailTemplateDO template);


    /**
     * 根据组织ID和模板ID查找模板
     */
    MarketingEmailTemplateDO findByOrgCodeAndTemplateId(String orgCode, String templateId);

    /**
     * 查找组织的所有启用模板
     */
    List<MarketingEmailTemplateDO> findActiveTemplatesByOrgCode(String orgCode);

    /**
     * 分页查询组织的所有模板
     */
    Page<MarketingEmailTemplateDO> findByOrgCode(String orgCode, Pageable pageable);

    /**
     * 根据组织ID和状态查询模板
     */
    Page<MarketingEmailTemplateDO> findByOrgCodeAndEnableStatus(String orgCode, EnableStatusEnum enableStatus, Pageable pageable);
    
    /**
     * 根据组织ID和状态查询模板列表
     */
    List<MarketingEmailTemplateDO> findByOrgCodeAndEnableStatus(String orgCode, EnableStatusEnum enableStatus);
    
    /**
     * 统计组织的模板数量
     */
    long countByOrgCodeAndEnableStatus(String orgCode, EnableStatusEnum enableStatus);

    /**
     * 根据示例查询
     */
    Page<MarketingEmailTemplateDO> findByExample(MarketingEmailTemplateDO example, Pageable pageable);

    /**
     * 根据模板ID查找模板（不区分组织）
     */
    MarketingEmailTemplateDO getByTemplateId(String templateId);

    /**
     * 根据模板代码查找模板
     */
    MarketingEmailTemplateDO findByTemplateCode(String templateCode);

    /**
     * 根据组织编码和模板代码查找模板
     */
    MarketingEmailTemplateDO findByOrgCodeAndTemplateCode(String orgCode, String templateCode);

    /**
     * 统计组织的模板数量
     */
    long countByOrgCode(String orgCode);

    /**
     * 根据条件查询模板
     */
    Page<MarketingEmailTemplateDO> findAll(Specification<MarketingEmailTemplateDO> spec, Pageable pageable);

    /**
     * 逻辑删除模板
     */
    void deleteByTemplateId(String templateId);

    /**
     * 根据组织编码和模板名称查询模板列表
     */
    List<MarketingEmailTemplateDO> findByOrgCodeAndTemplateName(String orgCode, String templateName);
} 