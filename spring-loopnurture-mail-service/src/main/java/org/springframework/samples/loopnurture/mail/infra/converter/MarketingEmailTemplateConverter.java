package org.springframework.samples.loopnurture.mail.infra.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.samples.loopnurture.mail.domain.model.MarketingEmailTemplateDO;
import org.springframework.samples.loopnurture.mail.domain.model.vo.MarketingEmailTemplateExtendsInfoVO;
import org.springframework.samples.loopnurture.mail.domain.enums.EnableStatusEnum;
import org.springframework.samples.loopnurture.mail.infra.po.MarketingEmailTemplatePO;
import org.springframework.samples.loopnurture.mail.server.controller.dto.CreateMarketingEmailTemplateRequest;
import org.springframework.samples.loopnurture.mail.server.controller.dto.ModifyMarketingEmailTemplateRequest;
import org.springframework.samples.loopnurture.mail.server.controller.dto.MarketingEmailTemplateResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.UUID;

/**
 * 营销邮件模板转换器
 */
@Component
@RequiredArgsConstructor
public class MarketingEmailTemplateConverter {
    
    private static final DateTimeFormatter TEMPLATE_NAME_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    private final ObjectMapper objectMapper;
    


    /**
     * 将领域对象转换为响应DTO
     */
    public MarketingEmailTemplateResponse toResponse(MarketingEmailTemplateDO entity) {
        if (entity == null) {
            return null;
        }
        
        MarketingEmailTemplateResponse response = new MarketingEmailTemplateResponse();
        response.setOrgCode(entity.getOrgCode());
        response.setTemplateId(entity.getTemplateId());
        response.setTemplateName(entity.getTemplateName());
        response.setContentType(entity.getContentType()!=null?entity.getContentType().getCode():null);
        response.setContentTemplate(entity.getContentTemplate());
        response.setAiStrategyVersion(entity.getAiStrategyVersion());
        response.setEnableStatus(entity.getEnableStatus()!=null?entity.getEnableStatus().getCode():null);
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

        MarketingEmailTemplateDO.MarketingEmailTemplateDOBuilder builder = MarketingEmailTemplateDO.builder()
            .orgCode(po.getOrgCode())
            .templateId(po.getTemplateId())
            .templateName(po.getTemplateName())
            .contentType(org.springframework.samples.loopnurture.mail.domain.enums.ContentTypeEnum.fromCode(po.getContentType()))
            .contentTemplate(po.getContentTemplate())
            .aiStrategyVersion(po.getAiStrategyVersion())
            .enableStatus(org.springframework.samples.loopnurture.mail.domain.enums.EnableStatusEnum.fromCode(po.getEnableStatus()));

        try {
            if (StringUtils.hasText(po.getExtendsInfo())) {
                builder.extendsInfo(objectMapper.readValue(po.getExtendsInfo(), MarketingEmailTemplateExtendsInfoVO.class));
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize extends info", e);
        }

        return builder
            .createdAt(po.getCreatedAt())
            .updatedAt(po.getUpdatedAt())
            .createdBy(po.getCreatedBy())
            .updatedBy(po.getUpdatedBy())
            .build();
    }

    /**
     * 将领域对象转换为持久化对象
     */
    public MarketingEmailTemplatePO toPO(MarketingEmailTemplateDO entity) {
        if (entity == null) {
            return null;
        }

        MarketingEmailTemplatePO po = new MarketingEmailTemplatePO();
        po.setOrgCode(entity.getOrgCode());
        po.setTemplateId(entity.getTemplateId());
        po.setTemplateName(entity.getTemplateName());
        po.setContentType(entity.getContentType()!=null?entity.getContentType().getCode():null);
        po.setContentTemplate(entity.getContentTemplate());
        po.setAiStrategyVersion(entity.getAiStrategyVersion());
        po.setEnableStatus(entity.getEnableStatus()!=null?entity.getEnableStatus().getCode():null);
        
        try {
            if (entity.getExtendsInfo() != null) {
                po.setExtendsInfo(objectMapper.writeValueAsString(entity.getExtendsInfo()));
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize extends info", e);
        }

        po.setCreatedAt(entity.getCreatedAt());
        po.setUpdatedAt(entity.getUpdatedAt());
        po.setCreatedBy(entity.getCreatedBy());
        po.setUpdatedBy(entity.getUpdatedBy());

        return po;
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

    private MarketingEmailTemplateExtendsInfoVO   convertToExtendsInfo(Object extendsInfo) {
        if (extendsInfo == null) {
            return null;
        }
        try {
            if (extendsInfo instanceof String) {
                return objectMapper.readValue((String) extendsInfo, MarketingEmailTemplateExtendsInfoVO.class);
            } else {
                return objectMapper.convertValue(extendsInfo, MarketingEmailTemplateExtendsInfoVO.class);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert extends info", e);
        }
    }
}