package org.springframework.samples.loopnurture.users.infra.po;

import lombok.Data;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

/**
 * 营销用户持久化对象
 */
@Data
@Entity
@org.hibernate.annotations.Where(clause = "deleted = false")
@Table(name = "marketing_user", indexes = {
    @Index(name = "idx_primary_email", columnList = "primary_email"),
    @Index(name = "idx_phone", columnList = "phone")
}, uniqueConstraints = {
    @UniqueConstraint(name = "uk_user_uniq", columnNames = "user_uniq"),
    @UniqueConstraint(name = "uk_oauth_user", columnNames = {"oauth_user_id", "auth_type"})
})
public class MarketingUserPO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 系统用户ID，使用雪花算法生成的全局唯一ID
     */
    @Column(name = "system_user_id", nullable = false, unique = true)
    private Long systemUserId;

    @Column(name = "user_uniq", nullable = false)
    private String userUniq;

    @Column(name = "auth_type", nullable = false, columnDefinition = "SMALLINT")
    private Short authType;

    @Column(name = "oauth_user_id")
    private String oauthUserId;

    @Column(name = "password")
    private String password;

    @Column(name = "user_type", columnDefinition = "SMALLINT")
    private Short userType;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "extends_info", columnDefinition = "jsonb")
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
    private Short accountStatus;

    @Column(name = "email_verified", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean emailVerified;

    @Column(name = "phone_verified", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean phoneVerified;

    @Column(name = "deleted", nullable = false)
    private Boolean deleted = false;

    @Column(name = "current_org_code")
    private String currentOrgCode;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (emailVerified == null) emailVerified = false;
        if (phoneVerified == null) phoneVerified = false;
        if (deleted == null) deleted = false;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
} 