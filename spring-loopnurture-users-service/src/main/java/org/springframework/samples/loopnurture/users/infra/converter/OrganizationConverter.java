package org.springframework.samples.loopnurture.users.infra.converter;

import org.springframework.samples.loopnurture.users.domain.model.OrganizationDO;
import org.springframework.samples.loopnurture.users.infra.po.OrganizationPO;
import org.springframework.stereotype.Component;

@Component
public class OrganizationConverter {
    
    public OrganizationDO toEntity(OrganizationPO po) {
        if (po == null) {
            return null;
        }
        
        OrganizationDO entity = new OrganizationDO();
        entity.setId(po.getId());
        entity.setOrgCode(po.getOrgCode());
        entity.setOrgName(po.getOrgName());
        entity.setOrgType(po.getOrgType());
        entity.setStatus(po.getStatus());
        entity.setMaxUsers(po.getMaxUsers());
        entity.setMaxTemplates(po.getMaxTemplates());
        entity.setMaxRules(po.getMaxRules());
        entity.setSettings(po.getSettings());
        entity.setCreatedAt(po.getCreatedAt());
        entity.setUpdatedAt(po.getUpdatedAt());
        entity.setCreatedBy(po.getCreatedBy());
        entity.setUpdatedBy(po.getUpdatedBy());
        return entity;
    }
    
    public OrganizationPO toPO(OrganizationDO entity) {
        if (entity == null) {
            return null;
        }
        
        OrganizationPO po = new OrganizationPO();
        po.setId(entity.getId());
        po.setOrgCode(entity.getOrgCode());
        po.setOrgName(entity.getOrgName());
        po.setOrgType(entity.getOrgType());
        po.setStatus(entity.getStatus());
        po.setMaxUsers(entity.getMaxUsers());
        po.setMaxTemplates(entity.getMaxTemplates());
        po.setMaxRules(entity.getMaxRules());
        po.setSettings(entity.getSettings());
        po.setCreatedAt(entity.getCreatedAt());
        po.setUpdatedAt(entity.getUpdatedAt());
        po.setCreatedBy(entity.getCreatedBy());
        po.setUpdatedBy(entity.getUpdatedBy());
        return po;
    }
} 