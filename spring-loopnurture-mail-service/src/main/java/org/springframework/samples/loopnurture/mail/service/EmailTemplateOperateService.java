package org.springframework.samples.loopnurture.mail.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import jakarta.validation.ValidationException;

import org.springframework.samples.loopnurture.mail.domain.model.MarketingEmailTemplateDO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.samples.loopnurture.mail.context.UserContext;
import org.springframework.samples.loopnurture.mail.domain.enums.ContentTypeEnum;
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
        String templateId =  LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmss")) + UUID.randomUUID().toString().substring(0, 8);
        MarketingEmailTemplateDO template = MarketingEmailTemplateDO.builder()
                .templateId(templateId)
                .templateName(request.getTemplateName())
                .contentType(ContentTypeEnum.fromCode(request.getContentType()))
                .contentTemplate(request.getContentTemplate())
                .aiStrategyVersion(request.getAiStrategyVersion())
                .enableStatus(EnableStatusEnum.ENABLED)
                .orgCode(UserContext.getOrgCode())
                .createdAt(new Date())
                .updatedAt(new Date())
                .createdBy(UserContext.getUserId())
                .updatedBy(UserContext.getUserId())
                .build();
        return templateRepository.save(template);
    }

    @Transactional
    public MarketingEmailTemplateDO modifyTemplate(ModifyMarketingEmailTemplateRequest request) {

        // 判断模板是否存在
        MarketingEmailTemplateDO existing = templateRepository.getByTemplateId(request.getTemplateId());
        if(existing == null){
            throw new IllegalArgumentException("Template not found");
        }
        // 判断name是否已存在，排除当前模板
        List<MarketingEmailTemplateDO> dup = templateRepository.findByOrgCodeAndTemplateName(UserContext.getOrgCode(), request.getTemplateName());
        dup.stream().filter(t -> !t.getTemplateId().equals(request.getTemplateId())).findFirst().ifPresent(t -> {
            throw new ValidationException("Template name already exists");
        });

        // 修改模板，设置输入的所有字段，如果为空则不修改
        existing.modifyTemplate(request.getTemplateName(), ContentTypeEnum.fromCode(request.getContentType()), request.getContentTemplate(), request.getAiStrategyVersion(), EnableStatusEnum.fromCode(request.getEnableStatus()), request.getInputContent());
        return templateRepository.save(existing);
    }

    @Transactional
    public void deleteTemplate(String templateId) {
        // 判断模板是否存在
        MarketingEmailTemplateDO template = templateRepository.getByTemplateId(templateId);
        if(template==null){
            throw new IllegalArgumentException("Template not found");
        }
        templateRepository.deleteByTemplateId(template.getTemplateId());
    }


} 