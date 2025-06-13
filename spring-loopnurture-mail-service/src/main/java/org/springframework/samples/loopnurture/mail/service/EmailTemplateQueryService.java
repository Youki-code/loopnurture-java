package org.springframework.samples.loopnurture.mail.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.samples.loopnurture.mail.domain.model.MarketingEmailTemplateDO;
import org.springframework.samples.loopnurture.mail.domain.repository.MarketingEmailTemplateRepository;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * 仅包含查询方法的邮件模板 Query Service
 */
@Service
@RequiredArgsConstructor
public class EmailTemplateQueryService {

    private final MarketingEmailTemplateRepository templateRepository;


    public List<String> queryByTemplateName(String orgCode, String templateName) {
        return templateRepository.findByOrgCode(orgCode, Pageable.unpaged())
                .stream()
                .filter(t -> templateName.equalsIgnoreCase(t.getTemplateName()))
                .map(MarketingEmailTemplateDO::getTemplateName)
                .toList();
    }

    public MarketingEmailTemplateDO getTemplate(String templateCode) {
        MarketingEmailTemplateDO t=templateRepository.findByTemplateCode(templateCode);
        if(t==null){throw new IllegalArgumentException("Template not found: "+templateCode);}return t;
    }

    public MarketingEmailTemplateDO getTemplateById(String templateId) {
        MarketingEmailTemplateDO t = templateRepository.getByTemplateId(templateId);
        if(t==null){throw new IllegalArgumentException("Template not found: "+templateId);}return t;
    }

    /**
     * 兼容旧调用名称
     */
    public MarketingEmailTemplateDO getByTemplateId(String templateId){
        return getTemplateById(templateId);
    }

    public Page<MarketingEmailTemplateDO> pageTemplates(String orgCode, String templateId, String templateName,
                                                       Integer contentType, List<Integer> enableStatusList,
                                                       Pageable pageable) {
        Specification<MarketingEmailTemplateDO> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.hasText(orgCode)) predicates.add(cb.equal(root.get("orgCode"), orgCode));
            if (StringUtils.hasText(templateId)) predicates.add(cb.equal(root.get("templateId"), templateId));
            if (StringUtils.hasText(templateName)) predicates.add(cb.like(root.get("templateName"), "%" + templateName + "%"));
            if (contentType != null) predicates.add(cb.equal(root.get("contentType"), contentType));
            if (enableStatusList != null && !enableStatusList.isEmpty()) predicates.add(root.get("enableStatus").in(enableStatusList));
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return templateRepository.findAll(spec, pageable);
    }
} 