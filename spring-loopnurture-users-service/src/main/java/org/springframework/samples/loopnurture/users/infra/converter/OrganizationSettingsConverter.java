package org.springframework.samples.loopnurture.users.infra.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.samples.loopnurture.users.domain.model.vo.OrganizationSettingsVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 组织设置转换器
 * 用于在数据库JSON字段和OrganizationSettingsVO对象之间进行转换
 */
@Converter(autoApply = true)
public class OrganizationSettingsConverter implements AttributeConverter<OrganizationSettingsVO, String> {
    private static final Logger logger = LoggerFactory.getLogger(OrganizationSettingsConverter.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(OrganizationSettingsVO attribute) {
        try {
            return attribute == null ? null : objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            logger.error("Error converting OrganizationSettingsVO to JSON", e);
            return null;
        }
    }

    @Override
    public OrganizationSettingsVO convertToEntityAttribute(String dbData) {
        try {
            return dbData == null ? null : objectMapper.readValue(dbData, OrganizationSettingsVO.class);
        } catch (JsonProcessingException e) {
            logger.error("Error converting JSON to OrganizationSettingsVO", e);
            return null;
        }
    }
} 