package org.springframework.samples.loopnurture.users.infra.converter;

import org.springframework.samples.loopnurture.users.domain.model.UserOrganizationDO;
import org.springframework.samples.loopnurture.users.domain.enums.UserRoleEnum;
import org.springframework.samples.loopnurture.users.infra.po.UserOrganizationPO;
import org.springframework.stereotype.Component;

/**
 * 用户-组织关联转换器
 * 负责DO和PO之间的转换
 */
@Component
public class UserOrganizationConverter {
    
    public UserOrganizationDO toDO(UserOrganizationPO po) {
        if (po == null) {
            return null;
        }
        
        UserOrganizationDO entity = new UserOrganizationDO();
        entity.setId(po.getId());
        entity.setSystemUserId(po.getSystemUserId());
        entity.setOrgCode(po.getOrgCode());
        entity.setRole(UserRoleEnum.fromCode(po.getRole() != null ? po.getRole().intValue() : null));
        entity.setCreatedAt(po.getCreatedAt());
        entity.setUpdatedAt(po.getUpdatedAt());
        entity.setCreatedBy(po.getCreatedBy());
        entity.setUpdatedBy(po.getUpdatedBy());
        
        return entity;
    }
    
    public UserOrganizationPO toPO(UserOrganizationDO entity) {
        if (entity == null) {
            return null;
        }
        
        UserOrganizationPO po = new UserOrganizationPO();
        po.setId(entity.getId());
        po.setSystemUserId(entity.getSystemUserId());
        po.setOrgCode(entity.getOrgCode());
        po.setRole(entity.getRole() != null ? entity.getRole().getCode().shortValue() : null);
        po.setCreatedAt(entity.getCreatedAt());
        po.setUpdatedAt(entity.getUpdatedAt());
        po.setCreatedBy(entity.getCreatedBy());
        po.setUpdatedBy(entity.getUpdatedBy());
        
        return po;
    }
} 