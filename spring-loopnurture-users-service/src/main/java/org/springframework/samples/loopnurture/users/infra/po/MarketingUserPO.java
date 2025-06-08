package org.springframework.samples.loopnurture.users.infra.po;

import lombok.Data;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * 营销用户持久化对象
 */
@Data
@Entity
@Table(name = "marketing_user", indexes = {
    @Index(name = "idx_primary_email", columnList = "primary_email"),
    @Index(name = "idx_phone", columnList = "phone"),
    @Index(name = "idx_org_id", columnList = "org_id")
}, uniqueConstraints = {
    @UniqueConstraint(name = "uk_user_uniq", columnNames = "user_uniq"),
    @UniqueConstraint(name = "uk_oauth_user", columnNames = {"oauth_user_id", "auth_type"})
})
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

    @Column(name = "user_uniq", nullable = false)
    private String userUniq;

    @Column(name = "auth_type", nullable = false, columnDefinition = "SMALLINT")
    private Integer authType;

    @Column(name = "oauth_user_id")
    private String oauthUserId;

    @Column(name = "password")
    private String password;

    @Column(name = "user_type", columnDefinition = "SMALLINT")
    private Integer userType;

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

    @Column(name = "account_status", columnDefinition = "SMALLINT")
    private Integer accountStatus;

    @Column(name = "email_verified", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean emailVerified;

    @Column(name = "phone_verified", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean phoneVerified;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
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
        if (emailVerified == null) emailVerified = false;
        if (phoneVerified == null) phoneVerified = false;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
} 