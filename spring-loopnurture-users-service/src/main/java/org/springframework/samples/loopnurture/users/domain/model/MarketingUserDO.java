package org.springframework.samples.loopnurture.users.domain.model;

import lombok.Data;
import org.springframework.samples.loopnurture.users.domain.enums.AuthTypeEnum;
import org.springframework.samples.loopnurture.users.domain.enums.AccountStatusEnum;
import org.springframework.samples.loopnurture.users.domain.enums.UserTypeEnum;
import org.springframework.samples.loopnurture.users.domain.enums.LanguagePreferenceEnum;
import org.springframework.samples.loopnurture.users.domain.model.vo.MarketingUserExtendsVO;
import java.time.LocalDateTime;

/**
 * 营销用户领域对象
 * 代表系统中的一个营销用户实体，包含用户的基本信息、认证信息、联系方式等
 */
@Data
public class MarketingUserDO {
    /**
     * 系统用户ID，使用雪花算法生成的全局唯一ID
     * 用于在整个系统中唯一标识一个用户
     */
    private Long systemUserId;
    
    
    /**
     * 用户唯一标识，用于业务层面的唯一标识，如用户名
     */
    private String userUniq;
    
    /**
     * 认证类型
     */
    private AuthTypeEnum authType;
    
    /**
     * OAuth提供商的用户ID
     * 当authType为OAuth类型时必填
     */
    private String oauthUserId;
    
    /**
     * 密码
     */
    private String password;
    
    /**
     * 用户类型
     */
    private UserTypeEnum userType;
    
    /**
     * 用户昵称
     */
    private String nickname;
    
    /**
     * 用户头像URL
     */
    private String avatarUrl;
    
    /**
     * 主要邮箱地址，用于系统通知和登录
     */
    private String primaryEmail;
    
    /**
     * 备用邮箱地址，用于账号恢复
     */
    private String backupEmail;
    
    /**
     * 手机号码，可用于登录和短信通知
     */
    private String phone;
    
    /**
     * 固定电话号码
     */
    private String telephone;
    
    /**
     * 语言偏好
     */
    private LanguagePreferenceEnum languagePreference;
    
    /**
     * 用户时区
     */
    private String timezone;
    
    /**
     * 账户状态
     */
    private AccountStatusEnum accountStatus;
    
    /**
     * 邮箱是否已验证
     */
    private Boolean emailVerified;
    
    /**
     * 手机号是否已验证
     */
    private Boolean phoneVerified;
    
    /**
     * 当前组织编码
     * 在多组织场景下标识当前激活的组织
     */
    private String currentOrgCode;
    
    /**
     * 用户扩展信息
     * 存储OAuth信息等扩展字段
     */
    private MarketingUserExtendsVO extendInfo;
    
    /**
     * 记录创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 记录最后更新时间
     */
    private LocalDateTime updatedAt;
    
    /**
     * 最后登录时间
     */
    private LocalDateTime lastLoginAt;

    /**
     * 获取有效的用户显示名称
     * 优先使用昵称，如果昵称为空则使用userUniq
     */
    public String getDisplayName() {
        return nickname != null && !nickname.isEmpty() ? nickname : userUniq;
    }

    /**
     * 是否是OAuth用户
     */
    public boolean isOAuthUser() {
        return authType != null && authType != AuthTypeEnum.PASSWORD;
    }

    /**
     * 是否是特定OAuth提供商的用户
     */
    public boolean isOAuthProvider(AuthTypeEnum provider) {
        return authType == provider;
    }
} 