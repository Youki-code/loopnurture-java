package org.springframework.samples.loopnurture.mail.infra.repository;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import org.springframework.samples.loopnurture.mail.domain.model.MarketingEmailTemplateDO;
import org.springframework.samples.loopnurture.mail.domain.repository.MarketingEmailTemplateRepository;
import org.springframework.samples.loopnurture.mail.domain.enums.EnableStatusEnum;
import org.springframework.samples.loopnurture.mail.infra.converter.MarketingEmailTemplateConverter;
import org.springframework.samples.loopnurture.mail.infra.mapper.JpaMarketingEmailTemplateMapper;
import org.springframework.samples.loopnurture.mail.infra.po.MarketingEmailTemplatePO;
import org.springframework.data.domain.PageRequest;
import org.springframework.samples.loopnurture.mail.domain.repository.dto.MarketingEmailTemplatePageQueryDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 营销邮件模板仓储实现类
 */
@Repository
@RequiredArgsConstructor
public class MarketingEmailTemplateRepositoryImpl implements MarketingEmailTemplateRepository {

    private final JpaMarketingEmailTemplateMapper templateMapper;
    private final MarketingEmailTemplateConverter templateConverter;

    @Override
    public MarketingEmailTemplateDO save(MarketingEmailTemplateDO template) {
        MarketingEmailTemplatePO po = templateConverter.toPO(template);
        // 如果主键为空，尝试根据 templateId 找到现有记录，确保执行更新而不是插入
        if (po.getId() == null && po.getTemplateId() != null) {
            templateMapper.findByTemplateId(po.getTemplateId())
                    .ifPresent(existing -> po.setId(existing.getId()));
        }
        return templateConverter.toDO(templateMapper.save(po));
    }


    @Override
    public MarketingEmailTemplateDO findByOrgCodeAndTemplateCode(String orgCode, String templateCode) {
        return templateMapper.findByOrgCodeAndTemplateId(orgCode, templateCode).map(templateConverter::toDO).orElse(null);
    }


    @Override
    public long countByOrgCode(String orgCode) {
        return templateMapper.countByOrgCode(orgCode);
    }

    @Override
    public Page<MarketingEmailTemplateDO> findByOrgCode(String orgCode, Pageable pageable) {
        return templateMapper.findByOrgCode(orgCode, pageable)
            .map(templateConverter::toDO);
    }



    @Override
    public MarketingEmailTemplateDO findByOrgCodeAndTemplateId(String orgCode, String templateId) {
        return templateMapper.findByOrgCodeAndTemplateId(orgCode, templateId).map(templateConverter::toDO).orElse(null);
    }

    @Override
    public List<MarketingEmailTemplateDO> findActiveTemplatesByOrgCode(String orgCode) {
        return templateMapper.findByOrgCodeAndEnableStatus(orgCode, EnableStatusEnum.ENABLED.getCode())
                .stream()
                .map(templateConverter::toDO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<MarketingEmailTemplateDO> findByOrgCodeAndEnableStatus(String orgCode, EnableStatusEnum enableStatus, Pageable pageable) {
        return templateMapper.findByOrgCodeAndEnableStatus(orgCode, enableStatus.getCode(), pageable)
                .map(templateConverter::toDO);
    }

    @Override
    public List<MarketingEmailTemplateDO> findByOrgCodeAndEnableStatus(String orgCode, EnableStatusEnum enableStatus) {
        return templateMapper.findByOrgCodeAndEnableStatus(orgCode, enableStatus.getCode())
                .stream()
                .map(templateConverter::toDO)
                .collect(Collectors.toList());
    }

    @Override
    public long countByOrgCodeAndEnableStatus(String orgCode, EnableStatusEnum enableStatus) {
        return templateMapper.countByOrgCodeAndEnableStatus(orgCode, enableStatus.getCode());
    }

    @Override
    public Page<MarketingEmailTemplateDO> findByExample(MarketingEmailTemplateDO example, Pageable pageable) {
        MarketingEmailTemplatePO po = templateConverter.toPO(example);
        return templateMapper.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            if (StringUtils.hasText(po.getOrgCode())) {
                predicates.add(cb.equal(root.get("orgCode"), po.getOrgCode()));
            }
            if (StringUtils.hasText(po.getTemplateId())) {
                predicates.add(cb.equal(root.get("templateId"), po.getTemplateId()));
            }
            if (StringUtils.hasText(po.getTemplateName())) {
                predicates.add(cb.like(root.get("templateName"), "%" + po.getTemplateName() + "%"));
            }
            if (po.getContentType() != null) {
                predicates.add(cb.equal(root.get("contentType"), po.getContentType()));
            }
            if (po.getEnableStatus() != null) {
                predicates.add(cb.equal(root.get("enableStatus"), po.getEnableStatus()));
            }
            
            return cb.and(predicates.toArray(new Predicate[0]));
        }, pageable).map(templateConverter::toDO);
    }
    

    @Override
    public Page<MarketingEmailTemplateDO> findAll(Specification<MarketingEmailTemplateDO> spec, Pageable pageable) {
        @SuppressWarnings("unchecked")
        Specification<MarketingEmailTemplatePO> poSpec = (Specification<MarketingEmailTemplatePO>) (Specification<?>) spec;
        return templateMapper.findAll(poSpec, pageable)
                .map(templateConverter::toDO);
    }

    @Override
    public MarketingEmailTemplateDO findByTemplateCode(String templateCode) {
        return templateMapper.findByTemplateId(templateCode).map(templateConverter::toDO).orElse(null);
    }

    @Override
    public MarketingEmailTemplateDO getByTemplateId(String templateId) {
        return templateMapper.findByTemplateId(templateId).map(templateConverter::toDO).orElse(null);
    }

    @Override
    public void deleteByTemplateId(String templateId) {
        templateMapper.softDeleteByTemplateId(templateId);
    }

    @Override
    public List<MarketingEmailTemplateDO> findByOrgCodeAndTemplateName(String orgCode, String templateName) {
        return templateMapper.findByOrgCodeAndTemplateName(orgCode, templateName)
                .stream()
                .map(templateConverter::toDO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<MarketingEmailTemplateDO> pageQuery(MarketingEmailTemplatePageQueryDTO query) {
        int pageIndex = Math.max(query.getPageNum() - 1, 0);
        Pageable pageable = PageRequest.of(pageIndex, query.getPageSize());

        return templateMapper.findAll((root, q, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (query.getOrgCode() != null && !query.getOrgCode().isBlank()) {
                predicates.add(cb.equal(root.get("orgCode"), query.getOrgCode()));
            }
            if (query.getTemplateId() != null && !query.getTemplateId().isBlank()) {
                predicates.add(cb.equal(root.get("templateId"), query.getTemplateId()));
            }
            if (query.getTemplateName() != null && !query.getTemplateName().isBlank()) {
                predicates.add(cb.like(root.get("templateName"), "%" + query.getTemplateName() + "%"));
            }
            if (query.getContentType() != null) {
                predicates.add(cb.equal(root.get("contentType"), query.getContentType()));
            }
            if (query.getEnableStatusList() != null && !query.getEnableStatusList().isEmpty()) {
                predicates.add(root.get("enableStatus").in(query.getEnableStatusList()));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        }, pageable).map(templateConverter::toDO);
    }

    // 以下方法可按需扩展，如删除或按Specification查询
} 