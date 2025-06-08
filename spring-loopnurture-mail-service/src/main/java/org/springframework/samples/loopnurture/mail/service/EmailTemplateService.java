package org.springframework.samples.loopnurture.mail.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.samples.loopnurture.mail.annotation.RequireLogin;
import org.springframework.samples.loopnurture.mail.context.UserContext;
import org.springframework.samples.loopnurture.mail.domain.model.MarketingEmailTemplateDO;
import org.springframework.samples.loopnurture.mail.domain.enums.EmailTemplateStatusEnum;
import org.springframework.samples.loopnurture.mail.domain.enums.TemplateTypeEnum;
import org.springframework.samples.loopnurture.mail.domain.enums.ContentTypeEnum;
import org.springframework.samples.loopnurture.mail.domain.repository.MarketingEmailTemplateRepository;
import org.springframework.samples.loopnurture.mail.exception.ResourceNotFoundException;
import org.springframework.samples.loopnurture.mail.exception.ValidationException;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailTemplateService {
    
    private final MarketingEmailTemplateRepository templateRepository;
    private final UserContext userContext;

    @RequireLogin
    @Transactional
    public MarketingEmailTemplateDO createTemplate(MarketingEmailTemplateDO template) {
        template.setOrgId(userContext.getOrgId());
        template.setCreatedBy(userContext.getUserId());
        template.setUpdatedBy(userContext.getUserId());
        
        // 新建模板默认为草稿状态
        template.setStatus(EmailTemplateStatusEnum.DRAFT);
        
        if (existsByTemplateCode(template.getOrgId(), template.getTemplateCode())) {
            throw new ValidationException("Template code already exists");
        }
        return templateRepository.save(template);
    }

    @RequireLogin
    @Transactional
    public MarketingEmailTemplateDO updateTemplate(MarketingEmailTemplateDO template) {
        MarketingEmailTemplateDO existing = getTemplate(template.getId());
        
        // 验证组织权限
        if (!existing.getOrgId().equals(userContext.getOrgId())) {
            throw new ValidationException("No permission to update this template");
        }
        
        // 已删除的模板不能更新
        if (existing.isDeleted()) {
            throw new ValidationException("Cannot update deleted template");
        }
        
        template.setUpdatedBy(userContext.getUserId());
        
        if (!existing.getTemplateCode().equals(template.getTemplateCode()) &&
            existsByTemplateCode(template.getOrgId(), template.getTemplateCode())) {
            throw new ValidationException("Template code already exists");
        }
        return templateRepository.save(template);
    }

    @RequireLogin
    @Transactional
    public void deleteTemplate(Long templateId) {
        MarketingEmailTemplateDO template = getTemplate(templateId);
        
        // 验证组织权限
        if (!template.getOrgId().equals(userContext.getOrgId())) {
            throw new ValidationException("No permission to delete this template");
        }
        
        // 已删除的模板不能重复删除
        if (template.isDeleted()) {
            throw new ValidationException("Template already deleted");
        }
        
        template.setStatus(EmailTemplateStatusEnum.DELETED);
        template.setUpdatedBy(userContext.getUserId());
        templateRepository.save(template);
    }

    @RequireLogin
    public MarketingEmailTemplateDO getTemplate(Long templateId) {
        MarketingEmailTemplateDO template = templateRepository.findById(templateId)
            .orElseThrow(() -> new ResourceNotFoundException("Template not found"));
            
        // 验证组织权限
        if (!template.getOrgId().equals(userContext.getOrgId())) {
            throw new ValidationException("No permission to access this template");
        }
        
        return template;
    }

    @RequireLogin
    public MarketingEmailTemplateDO getTemplateByCode(String templateCode) {
        return templateRepository.findByOrgIdAndTemplateCode(userContext.getOrgId(), templateCode)
            .orElseThrow(() -> new ResourceNotFoundException("Template not found"));
    }

    @RequireLogin
    public Page<MarketingEmailTemplateDO> getOrgTemplates(Pageable pageable) {
        return templateRepository.findByOrgId(userContext.getOrgId(), pageable);
    }

    @RequireLogin
    public Page<MarketingEmailTemplateDO> getActiveTemplates(Pageable pageable) {
        return templateRepository.findByOrgIdAndStatus(
            userContext.getOrgId(), 
            EmailTemplateStatusEnum.PUBLISHED, 
            pageable
        );
    }

    /**
     * 分页查询营销邮件模板
     */
    @RequireLogin
    public Page<MarketingEmailTemplateDO> pageTemplates(
            String templateCode,
            String templateName,
            Integer templateType,
            Integer contentType,
            Integer status,
            Pageable pageable) {
        
        // 构建查询条件
        MarketingEmailTemplateDO template = new MarketingEmailTemplateDO();
        template.setOrgId(userContext.getOrgId());
        
        if (templateCode != null) {
            template.setTemplateCode(templateCode);
        }
        if (templateName != null) {
            template.setTemplateName(templateName);
        }
        if (templateType != null) {
            template.setTemplateType(TemplateTypeEnum.fromCode(templateType));
        }
        if (contentType != null) {
            template.setContentType(ContentTypeEnum.fromCode(contentType));
        }
        if (status != null) {
            template.setStatus(EmailTemplateStatusEnum.fromCode(status));
        }
        
        return templateRepository.findByExample(template, pageable);
    }

    private boolean existsByTemplateCode(Long orgId, String templateCode) {
        return templateRepository.existsByOrgIdAndTemplateCode(orgId, templateCode);
    }
} 