package org.springframework.samples.loopnurture.users.infra.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.samples.loopnurture.users.domain.model.OrganizationDO;
import org.springframework.samples.loopnurture.users.domain.model.vo.OrganizationSettingsVO;
import org.springframework.samples.loopnurture.users.domain.enums.OrganizationStatusEnum;
import org.springframework.samples.loopnurture.users.domain.enums.OrganizationTypeEnum;
import org.springframework.samples.loopnurture.users.infra.po.OrganizationPO;
import org.springframework.stereotype.Component;

/**
 * 组织对象转换器
 */
@Component
public class OrganizationConverter {

    private final ObjectMapper objectMapper;

    public OrganizationConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * 将DO转换为PO
     */
    public OrganizationPO toPO(OrganizationDO organizationDO) {
        if (organizationDO == null) {
            return null;
        }

        return OrganizationPO.builder()
            .id(organizationDO.getId())
            .orgCode(organizationDO.getOrgCode())
            .orgName(organizationDO.getOrgName())
            .orgType(organizationDO.getOrgType() != null ? organizationDO.getOrgType().getCode().shortValue() : null)
            .status(organizationDO.getStatus() != null ? organizationDO.getStatus().getCode().shortValue() : null)
            .description(organizationDO.getDescription())
            .address(organizationDO.getAddress())
            .phone(organizationDO.getPhone())
            .email(organizationDO.getEmail())
            .website(organizationDO.getWebsite())
            .logoUrl(organizationDO.getLogoUrl())
            .maxUsers(organizationDO.getMaxUsers())
            .maxTemplates(organizationDO.getMaxTemplates())
            .maxRules(organizationDO.getMaxRules())
            .settings(toJsonString(organizationDO.getExtendsInfo()))
            .createdAt(organizationDO.getCreatedAt())
            .updatedAt(organizationDO.getUpdatedAt())
            .createdBy(organizationDO.getCreatedBy())
            .updatedBy(organizationDO.getUpdatedBy())
            .build();
    }

    /**
     * 将PO转换为DO
     */
    public OrganizationDO toDO(OrganizationPO organizationPO) {
        if (organizationPO == null) {
            return null;
        }

        OrganizationDO organizationDO = new OrganizationDO();
        organizationDO.setId(organizationPO.getId());
        organizationDO.setOrgCode(organizationPO.getOrgCode());
        organizationDO.setOrgName(organizationPO.getOrgName());
        organizationDO.setOrgType(OrganizationTypeEnum.fromCode(organizationPO.getOrgType() != null ? organizationPO.getOrgType().intValue() : null));
        organizationDO.setStatus(OrganizationStatusEnum.fromCode(organizationPO.getStatus() != null ? organizationPO.getStatus().intValue() : null));
        organizationDO.setDescription(organizationPO.getDescription());
        organizationDO.setAddress(organizationPO.getAddress());
        organizationDO.setPhone(organizationPO.getPhone());
        organizationDO.setEmail(organizationPO.getEmail());
        organizationDO.setWebsite(organizationPO.getWebsite());
        organizationDO.setLogoUrl(organizationPO.getLogoUrl());
        organizationDO.setMaxUsers(organizationPO.getMaxUsers());
        organizationDO.setMaxTemplates(organizationPO.getMaxTemplates());
        organizationDO.setMaxRules(organizationPO.getMaxRules());
        organizationDO.setExtendsInfo(fromJsonString(organizationPO.getSettings()));
        organizationDO.setCreatedAt(organizationPO.getCreatedAt());
        organizationDO.setUpdatedAt(organizationPO.getUpdatedAt());
        organizationDO.setCreatedBy(organizationPO.getCreatedBy());
        organizationDO.setUpdatedBy(organizationPO.getUpdatedBy());
        return organizationDO;
    }

    private String toJsonString(OrganizationSettingsVO settings) {
        if (settings == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(settings);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert settings to JSON", e);
        }
    }

    private OrganizationSettingsVO fromJsonString(String json) {
        if (json == null || json.isEmpty()) {
            return null;
        }
        try {
            return objectMapper.readValue(json, OrganizationSettingsVO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse settings JSON", e);
        }
    }
} 