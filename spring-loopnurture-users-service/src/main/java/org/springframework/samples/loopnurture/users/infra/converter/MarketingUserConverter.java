package org.springframework.samples.loopnurture.users.infra.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.samples.loopnurture.users.domain.enums.*;
import org.springframework.samples.loopnurture.users.domain.model.MarketingUserDO;
import org.springframework.samples.loopnurture.users.domain.model.vo.MarketingUserExtendsVO;
import org.springframework.samples.loopnurture.users.infra.po.MarketingUserPO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 营销用户转换器
 * 负责DO和PO之间的转换
 */
@Component
@RequiredArgsConstructor
public class MarketingUserConverter {
    
    private final ObjectMapper objectMapper;

    /**
     * PO转换为DO
     */
    public MarketingUserDO toDO(MarketingUserPO po) {
        if (po == null) {
            return null;
        }

        MarketingUserDO entity = new MarketingUserDO();
        entity.setId(po.getId());
        entity.setOrgId(po.getOrgId());
        entity.setUserUniq(po.getUserUniq());
        entity.setAuthType(AuthTypeEnum.valueOf(po.getAuthType()));
        entity.setOauthUserId(po.getOauthUserId());
        entity.setUserType(UserTypeEnum.valueOf(po.getUserType()));
        entity.setNickname(po.getNickname());
        entity.setAvatarUrl(po.getAvatarUrl());
        entity.setPrimaryEmail(po.getPrimaryEmail());
        entity.setBackupEmail(po.getBackupEmail());
        entity.setPhone(po.getPhone());
        entity.setTelephone(po.getTelephone());
        entity.setLanguagePreference(LanguagePreferenceEnum.valueOf(po.getLanguagePreference()));
        entity.setTimezone(po.getTimezone());
        entity.setAccountStatus(AccountStatusEnum.valueOf(po.getAccountStatus()));
        entity.setEmailVerified(po.getEmailVerified());
        entity.setPhoneVerified(po.getPhoneVerified());
        entity.setCreatedAt(po.getCreatedAt());
        entity.setUpdatedAt(po.getUpdatedAt());
        entity.setLastLoginAt(po.getLastLoginAt());

        // 转换扩展信息
        try {
            if (po.getExtendsInfo() != null) {
                entity.setExtendInfo(objectMapper.readValue(po.getExtendsInfo(), MarketingUserExtendsVO.class));
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse extends info", e);
        }

        return entity;
    }

    /**
     * DO转换为PO
     */
    public MarketingUserPO toPO(MarketingUserDO entity) {
        if (entity == null) {
            return null;
        }

        MarketingUserPO po = new MarketingUserPO();
        po.setId(entity.getId());
        po.setOrgId(entity.getOrgId());
        po.setUserUniq(entity.getUserUniq());
        po.setAuthType(entity.getAuthType().name());
        po.setOauthUserId(entity.getOauthUserId());
        po.setUserType(entity.getUserType().name());
        po.setNickname(entity.getNickname());
        po.setAvatarUrl(entity.getAvatarUrl());
        po.setPrimaryEmail(entity.getPrimaryEmail());
        po.setBackupEmail(entity.getBackupEmail());
        po.setPhone(entity.getPhone());
        po.setTelephone(entity.getTelephone());
        po.setLanguagePreference(entity.getLanguagePreference().name());
        po.setTimezone(entity.getTimezone());
        po.setAccountStatus(entity.getAccountStatus().name());
        po.setEmailVerified(entity.getEmailVerified());
        po.setPhoneVerified(entity.getPhoneVerified());
        po.setCreatedAt(entity.getCreatedAt());
        po.setUpdatedAt(entity.getUpdatedAt());
        po.setLastLoginAt(entity.getLastLoginAt());

        // 转换扩展信息
        try {
            if (entity.getExtendInfo() != null) {
                po.setExtendsInfo(objectMapper.writeValueAsString(entity.getExtendInfo()));
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize extends info", e);
        }

        return po;
    }
} 