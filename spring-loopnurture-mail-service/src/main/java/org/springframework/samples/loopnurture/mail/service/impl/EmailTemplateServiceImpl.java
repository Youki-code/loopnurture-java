package org.springframework.samples.loopnurture.mail.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.samples.loopnurture.mail.domain.model.MarketingEmailTemplateDO;
import org.springframework.samples.loopnurture.mail.domain.enums.EnableStatusEnum;
import org.springframework.samples.loopnurture.mail.infra.po.MarketingEmailTemplatePO;
import org.springframework.samples.loopnurture.mail.infra.converter.MarketingEmailTemplateConverter;
import org.springframework.samples.loopnurture.mail.infra.repository.MarketingEmailTemplateRepository;
import org.springframework.samples.loopnurture.mail.service.EmailTemplateService;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 邮件模板服务实现类
 */
@Service
@RequiredArgsConstructor
public class EmailTemplateServiceImpl implements EmailTemplateService {

    private final MarketingEmailTemplateRepository templateRepository;
    private final MarketingEmailTemplateConverter templateConverter;

    @Override
    @Transactional
    public MarketingEmailTemplateDO createTemplate(MarketingEmailTemplateDO template) {
        MarketingEmailTemplatePO po = templateConverter.toPO(template);
        po = templateRepository.save(po);
        return templateConverter.toDO(po);
    }

    @Override
    @Transactional
    public MarketingEmailTemplateDO updateTemplate(MarketingEmailTemplateDO template) {
        MarketingEmailTemplatePO po = templateRepository.findByTemplateCode(template.getTemplateCode())
            .orElseThrow(() -> new IllegalArgumentException("Template not found"));
        
        // 更新模板信息
        if (StringUtils.hasText(template.getTemplateName())) {
            po.setTemplateName(template.getTemplateName());
        }
        if (template.getContentType() != null) {
            po.setContentType(template.getContentType().getCode());
        }
        if (StringUtils.hasText(template.getContentTemplate())) {
            po.setContentTemplate(template.getContentTemplate());
        }
        if (StringUtils.hasText(template.getAiStrategyVersion())) {
            po.setAiStrategyVersion(template.getAiStrategyVersion());
        }
        if (template.getExtendsInfo() != null) {
            po.setExtendsInfo(template.getExtendsInfo());
        }
        
        po = templateRepository.save(po);
        return templateConverter.toDO(po);
    }

    @Override
    @Transactional
    public void deleteTemplate(String templateCode) {
        MarketingEmailTemplatePO po = templateRepository.findByTemplateCode(templateCode)
            .orElseThrow(() -> new IllegalArgumentException("Template not found"));
        po.setDeleted(1);
        templateRepository.save(po);
    }

    @Override
    @Transactional
    public MarketingEmailTemplateDO disableTemplate(String templateCode) {
        MarketingEmailTemplatePO po = templateRepository.findByTemplateCode(templateCode)
            .orElseThrow(() -> new IllegalArgumentException("Template not found"));
        po.setEnableStatus(EnableStatusEnum.DISABLED.getCode());
        po = templateRepository.save(po);
        return templateConverter.toDO(po);
    }

    @Override
    public MarketingEmailTemplateDO getTemplateByCode(String templateCode) {
        MarketingEmailTemplatePO po = templateRepository.findByTemplateCode(templateCode)
            .orElseThrow(() -> new IllegalArgumentException("Template not found"));
        return templateConverter.toDO(po);
    }

    @Override
    public Page<MarketingEmailTemplateDO> pageTemplates(
            String orgCode,
            String templateCode,
            String templateName,
            Integer contentType,
            List<Integer> enableStatusList,
            Pageable pageable) {
        
        Specification<MarketingEmailTemplatePO> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            // 组织编码
            if (StringUtils.hasText(orgCode)) {
                predicates.add(cb.equal(root.get("orgCode"), orgCode));
            }
            
            // 模板代码
            if (StringUtils.hasText(templateCode)) {
                predicates.add(cb.equal(root.get("templateCode"), templateCode));
            }
            
            // 模板名称
            if (StringUtils.hasText(templateName)) {
                predicates.add(cb.like(root.get("templateName"), "%" + templateName + "%"));
            }
            
            // 内容类型
            if (Objects.nonNull(contentType)) {
                predicates.add(cb.equal(root.get("contentType"), contentType));
            }
            
            // 启用状态列表
            if (enableStatusList != null && !enableStatusList.isEmpty()) {
                predicates.add(root.get("enableStatus").in(enableStatusList));
            }
            
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        
        return templateRepository.findAll(spec, pageable)
            .map(templateConverter::toDO);
    }
} 