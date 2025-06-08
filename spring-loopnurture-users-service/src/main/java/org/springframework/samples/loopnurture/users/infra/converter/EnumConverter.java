package org.springframework.samples.loopnurture.users.infra.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.samples.loopnurture.users.domain.enums.*;

/**
 * 枚举类型转换器
 */
public class EnumConverter {

    @Converter(autoApply = true)
    public static class UserTypeConverter implements AttributeConverter<UserTypeEnum, Integer> {
        @Override
        public Integer convertToDatabaseColumn(UserTypeEnum attribute) {
            return attribute == null ? null : attribute.getCode();
        }

        @Override
        public UserTypeEnum convertToEntityAttribute(Integer dbData) {
            return UserTypeEnum.fromCode(dbData);
        }
    }

    @Converter(autoApply = true)
    public static class AccountStatusConverter implements AttributeConverter<AccountStatusEnum, Integer> {
        @Override
        public Integer convertToDatabaseColumn(AccountStatusEnum attribute) {
            return attribute == null ? null : attribute.getCode();
        }

        @Override
        public AccountStatusEnum convertToEntityAttribute(Integer dbData) {
            return AccountStatusEnum.fromCode(dbData);
        }
    }

    @Converter(autoApply = true)
    public static class LanguagePreferenceConverter implements AttributeConverter<LanguagePreferenceEnum, String> {
        @Override
        public String convertToDatabaseColumn(LanguagePreferenceEnum attribute) {
            return attribute == null ? null : attribute.getCode();
        }

        @Override
        public LanguagePreferenceEnum convertToEntityAttribute(String dbData) {
            return LanguagePreferenceEnum.fromCode(dbData);
        }
    }

    @Converter(autoApply = true)
    public static class OrganizationTypeConverter implements AttributeConverter<OrganizationTypeEnum, Integer> {
        @Override
        public Integer convertToDatabaseColumn(OrganizationTypeEnum attribute) {
            return attribute == null ? null : attribute.getCode();
        }

        @Override
        public OrganizationTypeEnum convertToEntityAttribute(Integer dbData) {
            return OrganizationTypeEnum.fromCode(dbData);
        }
    }

    @Converter(autoApply = true)
    public static class OrganizationStatusConverter implements AttributeConverter<OrganizationStatusEnum, Integer> {
        @Override
        public Integer convertToDatabaseColumn(OrganizationStatusEnum attribute) {
            return attribute == null ? null : attribute.getCode();
        }

        @Override
        public OrganizationStatusEnum convertToEntityAttribute(Integer dbData) {
            return OrganizationStatusEnum.fromCode(dbData);
        }
    }

    @Converter(autoApply = true)
    public static class UserRoleConverter implements AttributeConverter<UserRoleEnum, Integer> {
        @Override
        public Integer convertToDatabaseColumn(UserRoleEnum attribute) {
            return attribute == null ? null : attribute.getCode();
        }

        @Override
        public UserRoleEnum convertToEntityAttribute(Integer dbData) {
            return UserRoleEnum.fromCode(dbData);
        }
    }

    @Converter(autoApply = true)
    public static class AuthTypeConverter implements AttributeConverter<AuthTypeEnum, Integer> {
        @Override
        public Integer convertToDatabaseColumn(AuthTypeEnum attribute) {
            return attribute == null ? null : attribute.getCode();
        }

        @Override
        public AuthTypeEnum convertToEntityAttribute(Integer dbData) {
            return AuthTypeEnum.fromCode(dbData);
        }
    }
} 