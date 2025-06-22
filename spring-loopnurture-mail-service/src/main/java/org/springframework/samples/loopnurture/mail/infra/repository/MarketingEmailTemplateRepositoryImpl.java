package org.springframework.samples.loopnurture.mail.infra.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.samples.loopnurture.mail.domain.model.MarketingEmailTemplateDO;
import org.springframework.samples.loopnurture.mail.domain.repository.MarketingEmailTemplateRepository;
import org.springframework.samples.loopnurture.mail.infra.converter.MarketingEmailTemplateConverter;
import org.springframework.samples.loopnurture.mail.infra.mapper.JpaMarketingEmailTemplateMapper;
import org.springframework.samples.loopnurture.mail.context.UserContext;

import java.util.Optional;

/**
 * 营销邮件模板仓储实现类
 */
@Repository
@RequiredArgsConstructor
public class MarketingEmailTemplateRepositoryImpl implements MarketingEmailTemplateRepository {

    private final JpaMarketingEmailTemplateMapper jpaMapper;
    private final MarketingEmailTemplateConverter converter;

    @Override
    public MarketingEmailTemplateDO save(MarketingEmailTemplateDO template) {
        // 根据 orgCode + templateId 判断是否已存在记录，若存在则更新，避免重复插入
        String orgCode = template.getOrgCode();
        String templateId = template.getTemplateId();

        org.springframework.samples.loopnurture.mail.infra.po.MarketingEmailTemplatePO po;
        if (orgCode != null && templateId != null) {
            var existingOpt = jpaMapper.findByOrgCodeAndTemplateId(orgCode, templateId);
            if (existingOpt.isPresent()) {
                // 使用 converter 填充最新字段，再保留原始 id 以便执行 update
                po = converter.toPO(template);
                po.setId(existingOpt.get().getId());
            } else {
                po = converter.toPO(template);
            }
        } else {
            po = converter.toPO(template);
        }
        jpaMapper.save(po);
        return converter.toDO(po);
    }

    @Override
    public MarketingEmailTemplateDO findById(Long id) {
        return jpaMapper.findById(id)
                .map(po -> converter.toDO(po))
                .orElse(null);
    }

    @Override
    public Optional<MarketingEmailTemplateDO> findByOrgCodeAndTemplateId(String orgCode, String templateId) {
        return jpaMapper.findByOrgCodeAndTemplateId(orgCode, templateId)
                .map(po -> converter.toDO(po));
    }

    @Override
    public Page<MarketingEmailTemplateDO> findByOrgCode(String orgCode, Pageable pageable) {
        return jpaMapper.findByOrgCode(orgCode, pageable)
                .map(po -> converter.toDO(po));
    }

    @Override
    public Page<MarketingEmailTemplateDO> findByOrgCodeAndEnableStatus(String orgCode, Integer enableStatus, Pageable pageable) {
        return jpaMapper.findByOrgCodeAndEnableStatus(orgCode, enableStatus.shortValue(), pageable)
                .map(po -> converter.toDO(po));
    }

    @Override
    public Page<MarketingEmailTemplateDO> findByOrgCodeAndContentType(String orgCode, Integer contentType, Pageable pageable) {
        return jpaMapper.findByOrgCodeAndContentType(orgCode, contentType.shortValue(), pageable)
                .map(po -> converter.toDO(po));
    }

    @Override
    public long countByOrgCode(String orgCode) {
        return jpaMapper.countByOrgCode(orgCode);
    }

    @Override
    public long countByOrgCodeAndEnableStatus(String orgCode, Integer enableStatus) {
        return jpaMapper.countByOrgCodeAndEnableStatus(orgCode, enableStatus.shortValue());
    }

    @Override
    public void deleteByOrgCodeAndTemplateId(String orgCode, String templateId) {
        jpaMapper.deleteByOrgCodeAndTemplateId(orgCode, templateId);
    }

    @Override
    public void deleteByOrgCode(String orgCode) {
        jpaMapper.deleteByOrgCode(orgCode);
    }

    @Override
    public boolean existsByOrgCodeAndTemplateId(String orgCode, String templateId) {
        return jpaMapper.existsByOrgCodeAndTemplateId(orgCode, templateId);
    }

    @Override
    public Page<MarketingEmailTemplateDO> findByOrgCodeAndTemplateNameContaining(String orgCode, String templateName, Pageable pageable) {
        return jpaMapper.findByOrgCodeAndTemplateNameContaining(orgCode, templateName, pageable)
                .map(po -> converter.toDO(po));
    }

    @Override
    public MarketingEmailTemplateDO getByTemplateId(String templateId) {
        String orgCode = UserContext.getOrgCode();
        return jpaMapper.findByOrgCodeAndTemplateId(orgCode, templateId)
                .map(po -> converter.toDO(po))
                .orElse(null);
    }

    @Override
    public java.util.List<MarketingEmailTemplateDO> findByOrgCodeAndTemplateName(String orgCode, String templateName) {
        org.springframework.data.domain.Page<org.springframework.samples.loopnurture.mail.infra.po.MarketingEmailTemplatePO> poPage =
                jpaMapper.findByOrgCodeAndTemplateNameContaining(orgCode, templateName, Pageable.unpaged());
        java.util.List<MarketingEmailTemplateDO> results = new java.util.ArrayList<>(poPage.getContent().size());
        for (org.springframework.samples.loopnurture.mail.infra.po.MarketingEmailTemplatePO po : poPage.getContent()) {
            results.add(converter.toDO(po));
        }
        return results;
    }

    @Override
    public void deleteByTemplateId(String templateId) {
        jpaMapper.deleteByOrgCodeAndTemplateId(UserContext.getOrgCode(), templateId);
    }

    @Override
    public Page<MarketingEmailTemplateDO> pageQuery(org.springframework.samples.loopnurture.mail.domain.repository.dto.MarketingEmailTemplatePageQueryDTO query) {
        Pageable pageable = org.springframework.data.domain.PageRequest.of(query.getPageNum(), query.getPageSize());

        // 如果只传入 orgCode+templateName 的场景沿用原查询；否则使用 Specification 组合条件
        boolean simpleNameSearch = query.getOrgCode() != null && query.getTemplateName() != null
                && query.getTemplateId() == null && query.getContentType() == null && (query.getEnableStatusList()==null || query.getEnableStatusList().isEmpty());
        if (simpleNameSearch) {
            return jpaMapper.findByOrgCodeAndTemplateNameContaining(query.getOrgCode(), query.getTemplateName(), pageable)
                    .map(po -> converter.toDO(po));
        }

        // 其余情况走 Specification
        org.springframework.data.jpa.domain.Specification<org.springframework.samples.loopnurture.mail.infra.po.MarketingEmailTemplatePO> spec = (root, q, cb) -> {
            java.util.List<jakarta.persistence.criteria.Predicate> predicates = new java.util.ArrayList<>();
            if (query.getOrgCode() != null) predicates.add(cb.equal(root.get("orgCode"), query.getOrgCode()));
            if (query.getTemplateId() != null) predicates.add(cb.equal(root.get("templateId"), query.getTemplateId()));
            if (query.getTemplateName() != null) predicates.add(cb.like(root.get("templateName"), "%" + query.getTemplateName() + "%"));
            if (query.getContentType() != null) predicates.add(cb.equal(root.get("contentType"), query.getContentType()));
            if (query.getEnableStatusList() != null && !query.getEnableStatusList().isEmpty()) predicates.add(root.get("enableStatus").in(query.getEnableStatusList()));
            return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        };
        org.springframework.data.domain.Page<org.springframework.samples.loopnurture.mail.infra.po.MarketingEmailTemplatePO> poPage = jpaMapper.findAll(spec, pageable);
        return poPage.map(po -> converter.toDO(po));
    }

    @Override
    public Page<MarketingEmailTemplateDO> findAll(org.springframework.data.jpa.domain.Specification<MarketingEmailTemplateDO> spec, Pageable pageable) {
        // 因为领域层 Specification 与 PO 类型不匹配，这里进行一次泛型擦除并显式转换
        @SuppressWarnings("unchecked")
        org.springframework.data.domain.Page<org.springframework.samples.loopnurture.mail.infra.po.MarketingEmailTemplatePO> poPage = (org.springframework.data.domain.Page<org.springframework.samples.loopnurture.mail.infra.po.MarketingEmailTemplatePO>)
                jpaMapper.findAll((org.springframework.data.jpa.domain.Specification) spec, pageable);
        return poPage.map(po -> converter.toDO(po));
    }
} 