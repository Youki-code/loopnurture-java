package org.springframework.samples.loopnurture.mail.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.samples.loopnurture.mail.domain.model.MarketingEmailTemplateDO;
import org.springframework.samples.loopnurture.mail.domain.enums.EmailTemplateStatusEnum;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import javax.persistence.criteria.Predicate;

/**
 * 营销邮件模板领域仓储接口
 */
public interface MarketingEmailTemplateRepository extends 
        JpaRepository<MarketingEmailTemplateDO, Long>,
        JpaSpecificationExecutor<MarketingEmailTemplateDO> {
    
    /**
     * 保存营销邮件模板
     */
    MarketingEmailTemplateDO save(MarketingEmailTemplateDO template);

    /**
     * 根据ID查找营销邮件模板
     */
    Optional<MarketingEmailTemplateDO> findById(Long id);

    /**
     * 根据组织ID和模板代码查找模板
     */
    Optional<MarketingEmailTemplateDO> findByOrgIdAndTemplateCode(Long orgId, String templateCode);

    /**
     * 查找组织的所有启用模板
     */
    List<MarketingEmailTemplateDO> findActiveTemplatesByOrgId(Long orgId);

    /**
     * 分页查询组织的所有模板
     */
    Page<MarketingEmailTemplateDO> findByOrgId(Long orgId, Pageable pageable);

    /**
     * 检查模板代码是否已存在
     */
    boolean existsByOrgIdAndTemplateCode(Long orgId, String templateCode);

    /**
     * 删除营销邮件模板
     */
    void deleteById(Long id);

    /**
     * 根据组织ID和状态查询模板
     */
    Page<MarketingEmailTemplateDO> findByOrgIdAndStatus(Long orgId, EmailTemplateStatusEnum status, Pageable pageable);
    
    /**
     * 根据组织ID和状态查询模板列表
     */
    List<MarketingEmailTemplateDO> findByOrgIdAndStatus(Long orgId, EmailTemplateStatusEnum status);
    
    /**
     * 统计组织的模板数量
     */
    long countByOrgIdAndStatus(Long orgId, EmailTemplateStatusEnum status);

    /**
     * 根据示例查询
     */
    default Page<MarketingEmailTemplateDO> findByExample(MarketingEmailTemplateDO example, Pageable pageable) {
        return findAll((root, query, builder) -> {
            var predicates = new ArrayList<Predicate>();
            
            // 组织ID
            predicates.add(builder.equal(root.get("orgId"), example.getOrgId()));
            
            // 模板代码
            if (example.getTemplateCode() != null) {
                predicates.add(builder.like(root.get("templateCode"), "%" + example.getTemplateCode() + "%"));
            }
            
            // 模板名称
            if (example.getTemplateName() != null) {
                predicates.add(builder.like(root.get("templateName"), "%" + example.getTemplateName() + "%"));
            }
            
            // 模板类型
            if (example.getTemplateType() != null) {
                predicates.add(builder.equal(root.get("templateType"), example.getTemplateType()));
            }
            
            // 内容类型
            if (example.getContentType() != null) {
                predicates.add(builder.equal(root.get("contentType"), example.getContentType()));
            }
            
            // 状态
            if (example.getStatus() != null) {
                predicates.add(builder.equal(root.get("status"), example.getStatus()));
            }
            
            return builder.and(predicates.toArray(new Predicate[0]));
        }, pageable);
    }
} 