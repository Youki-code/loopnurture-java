package org.springframework.samples.loopnurture.users.infra.po;

import lombok.Data;
import org.springframework.samples.loopnurture.users.domain.enums.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * 营销用户持久化对象
 */
@Data
@Entity
@Table(name = "t_marketing_user")
public class MarketingUserPO {
    @Id
    @Column(name = "id")
    private String id;

    /**
     * 系统用户ID，使用雪花算法生成的全局唯一ID
     */
    @Column(name = "system_user_id", nullable = false, unique = true)
    private Long systemUserId;

    @Column(name = "org_id")
    private String orgId;

    @Column(name = "user_uniq")
    private String userUniq;

    @Column(name = "auth_type")
    private String authType;

    @Column(name = "oauth_user_id")
    private String oauthUserId;

    @Column(name = "password")
    private String password;

    @Column(name = "user_type")
    private String userType;

    @Column(name = "extends_info", columnDefinition = "TEXT")
    private String extendsInfo;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "primary_email")
    private String primaryEmail;

    @Column(name = "backup_email")
    private String backupEmail;

    @Column(name = "phone")
    private String phone;

    @Column(name = "telephone")
    private String telephone;

    @Column(name = "language_preference")
    private String languagePreference;

    @Column(name = "timezone")
    private String timezone;

    @Column(name = "account_status")
    private String accountStatus;

    @Column(name = "email_verified")
    private Boolean emailVerified;

    @Column(name = "phone_verified")
    private Boolean phoneVerified;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "org_id", insertable = false, updatable = false)
    private OrganizationPO organization;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
} 