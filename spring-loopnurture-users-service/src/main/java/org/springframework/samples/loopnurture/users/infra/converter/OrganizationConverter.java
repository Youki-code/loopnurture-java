package org.springframework.samples.loopnurture.users.infra.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.samples.loopnurture.users.domain.model.OrganizationDO;
import org.springframework.samples.loopnurture.users.domain.enums.OrganizationTypeEnum;
import org.springframework.samples.loopnurture.users.domain.enums.OrganizationStatusEnum;
import org.springframework.samples.loopnurture.users.infra.po.OrganizationPO;
import org.springframework.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 组织转换器
 * 负责在领域对象和持久化对象之间进行转换
 */
@Component
@RequiredArgsConstructor
public class OrganizationConverter {
    
    private final ObjectMapper objectMapper;
    private static final Logger log = LoggerFactory.getLogger(OrganizationConverter.class);
    
    /**
     * 将持久化对象转换为领域对象
     */
    public OrganizationDO toDO(OrganizationPO po) {
        if (po == null) {
            return null;
        }

        OrganizationDO entity = new OrganizationDO();
        entity.setOrgCode(po.getOrgCode());
        entity.setOrgName(po.getOrgName());
        entity.setOrgType(po.getOrgType() != null ? OrganizationTypeEnum.fromCode(po.getOrgType().intValue()) : null);
        entity.setStatus(po.getStatus() != null ? OrganizationStatusEnum.fromCode(po.getStatus().intValue()) : null);
        entity.setPhone(po.getPhone());
        entity.setEmail(po.getEmail());
        entity.setWebsite(po.getWebsite());
        entity.setMaxUsers(po.getMaxUsers());
        entity.setMaxTemplates(po.getMaxTemplates());
        entity.setMaxRules(po.getMaxRules());
        entity.setCreatedAt(po.getCreatedAt());
        entity.setUpdatedAt(po.getUpdatedAt());
        entity.setCreatedBy(po.getCreatedBy());
        entity.setUpdatedBy(po.getUpdatedBy());

        // 扩展信息 JSON -> VO
        if (StringUtils.hasText(po.getExtendsInfo())) {
            try {
                entity.setExtendsInfo(objectMapper.readValue(po.getExtendsInfo(),
                        org.springframework.samples.loopnurture.users.domain.model.vo.OrganizationSettingsVO.class));
            } catch (JsonProcessingException e) {
                log.error("Failed to parse extendsInfo JSON: {}", po.getExtendsInfo(), e);
            }
        }

        return entity;
    }

    /**
     * 将领域对象转换为持久化对象
     */
    public OrganizationPO toPO(OrganizationDO entity) {
        if (entity == null) {
            return null;
        }

        OrganizationPO po = new OrganizationPO();
        po.setOrgCode(entity.getOrgCode());
        po.setOrgName(entity.getOrgName());
        po.setOrgType(entity.getOrgType() != null ? entity.getOrgType().getCode().shortValue() : null);
        po.setStatus(entity.getStatus() != null ? entity.getStatus().getCode().shortValue() : null);
        po.setPhone(entity.getPhone());
        po.setEmail(entity.getEmail());
        po.setWebsite(entity.getWebsite());
        po.setMaxUsers(entity.getMaxUsers());
        po.setMaxTemplates(entity.getMaxTemplates());
        po.setMaxRules(entity.getMaxRules());
        po.setCreatedAt(entity.getCreatedAt());
        po.setUpdatedAt(entity.getUpdatedAt());
        po.setCreatedBy(entity.getCreatedBy());
        po.setUpdatedBy(entity.getUpdatedBy());

        // 扩展信息 VO -> JSON
        if (entity.getExtendsInfo() != null) {
            try {
                po.setExtendsInfo(objectMapper.writeValueAsString(entity.getExtendsInfo()));
            } catch (JsonProcessingException e) {
                log.error("Failed to serialize extendInfo: {}", entity.getExtendsInfo(), e);
            }
        }

        return po;
    }
} 