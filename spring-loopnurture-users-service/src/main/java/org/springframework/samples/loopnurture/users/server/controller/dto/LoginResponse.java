package org.springframework.samples.loopnurture.users.server.controller.dto;

import lombok.Data;
import org.springframework.samples.loopnurture.users.domain.model.MarketingUserDO;

/**
 * 登录响应对象
 */
@Data
public class LoginResponse {
    /**
     * JWT令牌
     */
    private String token;

    /**
     * 用户信息
     */
    private UserInfo userInfo;

    @Data
    public static class UserInfo {
        /**
         * 系统用户ID
         */
        private Long systemUserId;

        /**
         * 用户唯一标识
         */
        private String userUniq;

        /**
         * 用户昵称
         */
        private String nickname;

        /**
         * 用户主邮箱
         */
        private String primaryEmail;

        /**
         * 用户电话
         */
        private String phone;

        /**
         * 头像URL
         */
        private String avatarUrl;

        /**
         * 语言偏好
         */
        private String languagePreference;

        /**
         * 时区
         */
        private String timezone;

        /**
         * 认证类型
         */
        private Integer authType;

        /**
         * 组织编码（兼容单组织场景）
         */
        private String orgCode;

        /**
         * 邮箱验证
         */
        private Boolean emailVerified;

        /**
         * 电话验证
         */
        private Boolean phoneVerified;

        /**
         * 从DO对象创建UserInfo
         */
        public static UserInfo fromDO(MarketingUserDO user) {
            UserInfo info = new UserInfo();
            info.setSystemUserId(user.getSystemUserId());
            info.setUserUniq(user.getUserUniq());
            info.setNickname(user.getNickname());
            info.setPrimaryEmail(user.getPrimaryEmail());
            info.setPhone(user.getPhone());
            info.setAvatarUrl(user.getAvatarUrl());
            info.setLanguagePreference(user.getLanguagePreference() != null ? user.getLanguagePreference().toString() : null);
            info.setTimezone(user.getTimezone());
            info.setAuthType(user.getAuthType() != null ? user.getAuthType().getCode() : null);
            info.setOrgCode(null);
            info.setEmailVerified(user.getEmailVerified());
            info.setPhoneVerified(user.getPhoneVerified());
            return info;
        }
    }
} 