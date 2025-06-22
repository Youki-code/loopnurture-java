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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.UUID;

/**
 * 营销邮件模板转换器
 * 负责在领域对象和持久化对象之间进行转换
 */
@Component
@RequiredArgsConstructor
public class MarketingEmailTemplateConverter {
    
    private final ObjectMapper objectMapper;
    private static final Logger log = LoggerFactory.getLogger(MarketingEmailTemplateConverter.class);
    private static final DateTimeFormatter TEMPLATE_NAME_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
    
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

        MarketingEmailTemplateDO entity = new MarketingEmailTemplateDO();
        entity.setOrgCode(po.getOrgCode());
        entity.setTemplateId(po.getTemplateId());
        entity.setTemplateName(po.getTemplateName());
        entity.setContentType(po.getContentType() != null ? org.springframework.samples.loopnurture.mail.domain.enums.ContentTypeEnum.fromCode(po.getContentType().intValue()) : null);
        entity.setContentTemplate(po.getContentTemplate());
        entity.setAiStrategyVersion(po.getAiStrategyVersion());
        entity.setEnableStatus(po.getEnableStatus() != null ? org.springframework.samples.loopnurture.mail.domain.enums.EnableStatusEnum.fromCode(po.getEnableStatus().intValue()) : null);
        entity.setCreatedAt(po.getCreatedAt());
        entity.setUpdatedAt(po.getUpdatedAt());
        entity.setCreatedBy(po.getCreatedBy());
        entity.setUpdatedBy(po.getUpdatedBy());

        // 扩展信息 JSON -> VO
        if (StringUtils.hasText(po.getExtendsInfo())) {
            try {
                entity.setExtendsInfo(objectMapper.readValue(po.getExtendsInfo(), MarketingEmailTemplateExtendsInfoVO.class));
            } catch (JsonProcessingException e) {
                log.error("Failed to parse extendsInfo JSON: {}", po.getExtendsInfo(), e);
            }
        }

        return entity;
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
        po.setContentType(entity.getContentType()!=null?entity.getContentType().getCode().shortValue():null);
        po.setContentTemplate(entity.getContentTemplate());
        po.setAiStrategyVersion(entity.getAiStrategyVersion());
        po.setEnableStatus(entity.getEnableStatus()!=null?entity.getEnableStatus().getCode().shortValue():null);
        
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