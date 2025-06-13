package org.springframework.samples.loopnurture.mail.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import jakarta.validation.ValidationException;

import org.springframework.samples.loopnurture.mail.domain.model.MarketingEmailTemplateDO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import org.springframework.samples.loopnurture.mail.context.UserContext;
import org.springframework.samples.loopnurture.mail.domain.enums.EnableStatusEnum;
import org.springframework.samples.loopnurture.mail.domain.repository.MarketingEmailTemplateRepository;
import org.springframework.samples.loopnurture.mail.infra.converter.MarketingEmailTemplateConverter;
import org.springframework.samples.loopnurture.mail.server.controller.dto.CreateMarketingEmailTemplateRequest;
import org.springframework.samples.loopnurture.mail.server.controller.dto.ModifyMarketingEmailTemplateRequest;

import lombok.RequiredArgsConstructor;

/**
 * 邮件模板服务
 */
@Service
@RequiredArgsConstructor
public class EmailTemplateOperateService {

    private final MarketingEmailTemplateRepository templateRepository;
    private final MarketingEmailTemplateConverter templateConverter; // 保留以便未来扩展

    @Transactional
    public MarketingEmailTemplateDO createTemplate(CreateMarketingEmailTemplateRequest request) {
        // 判断templateName是否已存在
        List<MarketingEmailTemplateDO> dup = templateRepository.findByOrgCodeAndTemplateName(UserContext.getOrgCode(), request.getTemplateName());
        if (!CollectionUtils.isEmpty(dup)) {
            throw new ValidationException("Template name already exists");
        }
        // 创建模板，使用 UUID +日期前缀生成唯一模板 ID
        String templateId =  LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + UUID.randomUUID().toString().substring(0, 8);
        MarketingEmailTemplateDO template = MarketingEmailTemplateDO.builder()
                .templateId(templateId)
                .templateName(request.getTemplateName())
                .contentType(org.springframework.samples.loopnurture.mail.domain.enums.ContentTypeEnum.fromCode(request.getContentType()))
                .contentTemplate(request.getContentTemplate())
                .aiStrategyVersion(request.getAiStrategyVersion())
                .enableStatus(EnableStatusEnum.ENABLED)
                .orgCode(UserContext.getOrgCode())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .createdBy(UserContext.getUserId())
                .updatedBy(UserContext.getUserId())
                .build();
        return templateRepository.save(template);
    }

    @Transactional
    public MarketingEmailTemplateDO modifyTemplate(ModifyMarketingEmailTemplateRequest request) {
        MarketingEmailTemplateDO template = templateConverter.toEntity(request);
        MarketingEmailTemplateDO existing = templateRepository.findByTemplateCode(template.getTemplateCode());
        if(existing==null){
            throw new IllegalArgumentException("Template not found");
        }

        if (StringUtils.hasText(template.getTemplateName())) {
            existing.setTemplateName(template.getTemplateName());
        }
        if (template.getContentType() != null) {
            existing.setContentType(template.getContentType());
        }
        if (StringUtils.hasText(template.getContentTemplate())) {
            existing.setContentTemplate(template.getContentTemplate());
        }
        if (StringUtils.hasText(template.getAiStrategyVersion())) {
            existing.setAiStrategyVersion(template.getAiStrategyVersion());
        }
        if (template.getExtendsInfo() != null) {
            existing.setExtendsInfo(template.getExtendsInfo());
        }

        return templateRepository.save(existing);
    }

    @Transactional
    public void deleteTemplateByCode(String templateCode) {
        MarketingEmailTemplateDO template = templateRepository.findByTemplateCode(templateCode);
        if(template==null){
            throw new IllegalArgumentException("Template not found");
        }
        templateRepository.deleteByTemplateId(template.getTemplateId());
    }

    @Transactional
    public MarketingEmailTemplateDO disableTemplate(String templateCode) {
        MarketingEmailTemplateDO template = templateRepository.findByTemplateCode(templateCode);
        if(template==null){
            throw new IllegalArgumentException("Template not found");
        }
        template.setEnableStatus(EnableStatusEnum.DISABLED);
        return templateRepository.save(template);
    }


    public void deleteTemplate(String templateId) {
        templateRepository.deleteByTemplateId(templateId);
    }
} 