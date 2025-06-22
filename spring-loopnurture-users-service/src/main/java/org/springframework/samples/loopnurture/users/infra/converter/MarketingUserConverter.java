package org.springframework.samples.loopnurture.users.infra.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.samples.loopnurture.users.domain.model.MarketingUserDO;
import org.springframework.samples.loopnurture.users.domain.enums.AuthTypeEnum;
import org.springframework.samples.loopnurture.users.domain.enums.AccountStatusEnum;
import org.springframework.samples.loopnurture.users.domain.enums.UserTypeEnum;
import org.springframework.samples.loopnurture.users.domain.enums.LanguagePreferenceEnum;
import org.springframework.samples.loopnurture.users.infra.po.MarketingUserPO;
import org.springframework.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 营销用户转换器
 * 负责在领域对象和持久化对象之间进行转换
 */
@Component
@RequiredArgsConstructor
public class MarketingUserConverter {
    
    private final ObjectMapper objectMapper;
    private static final Logger log = LoggerFactory.getLogger(MarketingUserConverter.class);

    /**
     * 将持久化对象转换为领域对象
     */
    public MarketingUserDO toDO(MarketingUserPO po) {
        if (po == null) {
            return null;
        }

        MarketingUserDO entity = new MarketingUserDO();
        entity.setSystemUserId(po.getSystemUserId());
        entity.setUserUniq(po.getUserUniq());
        entity.setAuthType(po.getAuthType() != null ? AuthTypeEnum.fromCode(po.getAuthType().intValue()) : null);
        entity.setOauthUserId(po.getOauthUserId());
        entity.setPassword(po.getPassword());
        entity.setUserType(po.getUserType() != null ? UserTypeEnum.fromCode(po.getUserType().intValue()) : null);
        entity.setNickname(po.getNickname());
        entity.setAvatarUrl(po.getAvatarUrl());
        entity.setPrimaryEmail(po.getPrimaryEmail());
        entity.setBackupEmail(po.getBackupEmail());
        entity.setPhone(po.getPhone());
        entity.setTelephone(po.getTelephone());
        entity.setLanguagePreference(po.getLanguagePreference() != null ? LanguagePreferenceEnum.fromCode(po.getLanguagePreference()) : null);
        entity.setTimezone(po.getTimezone());
        entity.setAccountStatus(po.getAccountStatus() != null ? AccountStatusEnum.fromCode(po.getAccountStatus().intValue()) : null);
        entity.setEmailVerified(po.getEmailVerified());
        entity.setPhoneVerified(po.getPhoneVerified());
        entity.setCurrentOrgCode(po.getCurrentOrgCode());
        entity.setCreatedAt(po.getCreatedAt());
        entity.setUpdatedAt(po.getUpdatedAt());
        entity.setLastLoginAt(po.getLastLoginAt());

        // 扩展信息 JSON -> VO
        if (StringUtils.hasText(po.getExtendsInfo())) {
            try {
                entity.setExtendInfo(objectMapper.readValue(po.getExtendsInfo(),
                        org.springframework.samples.loopnurture.users.domain.model.vo.MarketingUserExtendsVO.class));
            } catch (JsonProcessingException e) {
                log.error("Failed to parse extendsInfo JSON: {}", po.getExtendsInfo(), e);
            }
        }

        return entity;
    }

    /**
     * 将领域对象转换为持久化对象
     */
    public MarketingUserPO toPO(MarketingUserDO entity) {
        if (entity == null) {
            return null;
        }

        MarketingUserPO po = new MarketingUserPO();
        po.setSystemUserId(entity.getSystemUserId());
        po.setUserUniq(entity.getUserUniq());
        po.setAuthType(entity.getAuthType() != null ? entity.getAuthType().getCode().shortValue() : null);
        po.setOauthUserId(entity.getOauthUserId());
        po.setPassword(entity.getPassword());
        po.setUserType(entity.getUserType() != null ? entity.getUserType().getCode().shortValue() : null);
        po.setNickname(entity.getNickname());
        po.setAvatarUrl(entity.getAvatarUrl());
        po.setPrimaryEmail(entity.getPrimaryEmail());
        po.setBackupEmail(entity.getBackupEmail());
        po.setPhone(entity.getPhone());
        po.setTelephone(entity.getTelephone());
        po.setLanguagePreference(entity.getLanguagePreference() != null ? entity.getLanguagePreference().getCode() : null);
        po.setTimezone(entity.getTimezone());
        po.setAccountStatus(entity.getAccountStatus() != null ? entity.getAccountStatus().getCode().shortValue() : null);
        po.setEmailVerified(entity.getEmailVerified());
        po.setPhoneVerified(entity.getPhoneVerified());
        po.setCurrentOrgCode(entity.getCurrentOrgCode());
        po.setCreatedAt(entity.getCreatedAt());
        po.setUpdatedAt(entity.getUpdatedAt());
        po.setLastLoginAt(entity.getLastLoginAt());

        // 扩展信息 VO -> JSON
        if (entity.getExtendInfo() != null) {
            try {
                po.setExtendsInfo(objectMapper.writeValueAsString(entity.getExtendInfo()));
            } catch (JsonProcessingException e) {
                log.error("Failed to serialize extendInfo: {}", entity.getExtendInfo(), e);
            }
        }

        return po;
    }
} 