package org.springframework.samples.loopnurture.mail.infra.converter;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.samples.loopnurture.mail.domain.model.MarketingEmailTemplateDO;
import org.springframework.samples.loopnurture.mail.domain.enums.EnableStatusEnum;
import org.springframework.samples.loopnurture.mail.domain.enums.ContentTypeEnum;
import org.springframework.samples.loopnurture.mail.infra.po.MarketingEmailTemplatePO;
import org.springframework.samples.loopnurture.mail.server.controller.dto.CreateMarketingEmailTemplateRequest;
import org.springframework.samples.loopnurture.mail.server.controller.dto.ModifyMarketingEmailTemplateRequest;
import org.springframework.samples.loopnurture.mail.server.controller.dto.MarketingEmailTemplateResponse;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * 营销邮件模板转换器
 */
@Component
public class MarketingEmailTemplateConverter {
    
    private static final DateTimeFormatter TEMPLATE_NAME_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    
    /**
     * 将创建请求DTO转换为领域对象
     */
    public MarketingEmailTemplateDO toEntity(CreateMarketingEmailTemplateRequest request) {
        if (request == null) {
            return null;
        }
        
        MarketingEmailTemplateDO entity = new MarketingEmailTemplateDO();
        String templateId = generateTemplateId();
        entity.setId(UUID.randomUUID().toString());
        entity.setTemplateId(templateId);
        entity.setTemplateName(generateTemplateName(request.getTemplateName(), templateId));
        entity.setContentType(ContentTypeEnum.fromCode(request.getContentType()));
        entity.setContentTemplate(request.getContentTemplate());
        entity.setAiStrategyVersion(request.getAiStrategyVersion());
        entity.setEnableStatus(EnableStatusEnum.ENABLED); // 默认启用
        entity.setDeleted(0); // 默认未删除
        entity.setExtendsInfo(request.getExtendsInfo());
        return entity;
    }

    /**
     * 将修改请求DTO转换为领域对象
     */
    public MarketingEmailTemplateDO toEntity(ModifyMarketingEmailTemplateRequest request) {
        if (request == null) {
            return null;
        }
        
        MarketingEmailTemplateDO entity = new MarketingEmailTemplateDO();
        entity.setTemplateId(request.getTemplateId());
        entity.setTemplateName(request.getTemplateName());
        entity.setContentType(ContentTypeEnum.fromCode(request.getContentType()));
        entity.setContentTemplate(request.getContentTemplate());
        entity.setAiStrategyVersion(request.getAiStrategyVersion());
        entity.setExtendsInfo(request.getExtendsInfo());
        return entity;
    }

    /**
     * 将领域对象转换为响应DTO
     */
    public MarketingEmailTemplateResponse toResponse(MarketingEmailTemplateDO entity) {
        if (entity == null) {
            return null;
        }
        
        MarketingEmailTemplateResponse response = new MarketingEmailTemplateResponse();
        response.setId(entity.getId());
        response.setOrgCode(entity.getOrgCode());
        response.setTemplateId(entity.getTemplateId());
        response.setTemplateName(entity.getTemplateName());
        response.setContentType(entity.getContentType().getCode());
        response.setContentTemplate(entity.getContentTemplate());
        response.setAiStrategyVersion(entity.getAiStrategyVersion());
        response.setEnableStatus(entity.getEnableStatus().getCode());
        response.setExtendsInfo(entity.getExtendsInfo());
        response.setCreatedAt(entity.getCreatedAt());
        response.setUpdatedAt(entity.getUpdatedAt());
        response.setCreatedBy(entity.getCreatedBy());
        response.setUpdatedBy(entity.getUpdatedBy());
        return response;
    }

    /**
     * 将持久化对象转换为领域对象
     */
    public MarketingEmailTemplateDO toDO(MarketingEmailTemplatePO po) {
        if (po == null) {
            return null;
        }
        
        MarketingEmailTemplateDO entity = new MarketingEmailTemplateDO();
        entity.setId(po.getId());
        entity.setOrgCode(po.getOrgCode());
        entity.setTemplateId(po.getTemplateId());
        entity.setTemplateName(po.getTemplateName());
        entity.setContentType(ContentTypeEnum.fromCode(po.getContentType()));
        entity.setContentTemplate(po.getContentTemplate());
        entity.setAiStrategyVersion(po.getAiStrategyVersion());
        entity.setEnableStatus(EnableStatusEnum.fromCode(po.getEnableStatus()));
        entity.setDeleted(po.getDeleted());
        entity.setExtendsInfo(po.getExtendsInfo());
        entity.setCreatedAt(po.getCreatedAt());
        entity.setUpdatedAt(po.getUpdatedAt());
        entity.setCreatedBy(po.getCreatedBy());
        entity.setUpdatedBy(po.getUpdatedBy());
        return entity;
    }

    /**
     * 生成模板ID
     * 格式：TPL + 随机8位数字
     */
    private String generateTemplateId() {
        return "TPL" + String.format("%08d", (int)(Math.random() * 100000000));
    }

    /**
     * 生成模板名称
     * 如果未提供名称，则使用"Template_[模板ID]_[时间戳]"格式生成
     */
    private String generateTemplateName(String providedName, String templateId) {
        if (StringUtils.hasText(providedName)) {
            return providedName;
        }
        return String.format("Template_%s_%s", 
            templateId,
            LocalDateTime.now().format(TEMPLATE_NAME_DATE_FORMATTER));
    }
}