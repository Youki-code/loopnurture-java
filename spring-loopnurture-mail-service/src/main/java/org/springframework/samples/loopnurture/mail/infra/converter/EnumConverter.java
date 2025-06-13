package org.springframework.samples.loopnurture.mail.infra.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.samples.loopnurture.mail.domain.enums.*;

/**
 * 枚举类型转换器
 */
public class EnumConverter {

    @Converter(autoApply = true)
    public static class EmailStatusConverter implements AttributeConverter<EmailStatusEnum, Integer> {
        @Override
        public Integer convertToDatabaseColumn(EmailStatusEnum attribute) {
            return attribute == null ? null : attribute.getCode();
        }

        @Override
        public EmailStatusEnum convertToEntityAttribute(Integer dbData) {
            return EmailStatusEnum.fromCode(dbData);
        }
    }

    @Converter(autoApply = true)
    public static class EmailTemplateStatusConverter implements AttributeConverter<EmailTemplateStatusEnum, Integer> {
        @Override
        public Integer convertToDatabaseColumn(EmailTemplateStatusEnum attribute) {
            return attribute == null ? null : attribute.getCode();
        }

        @Override
        public EmailTemplateStatusEnum convertToEntityAttribute(Integer dbData) {
            return EmailTemplateStatusEnum.fromCode(dbData);
        }
    }
} 