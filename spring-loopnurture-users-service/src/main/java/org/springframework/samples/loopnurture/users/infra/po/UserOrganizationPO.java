package org.springframework.samples.loopnurture.users.infra.po;

import lombok.Data;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * 用户-组织关联持久化对象
 */
@Data
@Entity
@Table(name = "user_organization", indexes = {
    @Index(name = "idx_user_org_user_id", columnList = "system_user_id"),
    @Index(name = "idx_user_org_org_id", columnList = "org_id")
}, uniqueConstraints = {
    @UniqueConstraint(name = "uk_user_org", columnNames = {"system_user_id", "org_id"})
})
public class UserOrganizationPO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "system_user_id", nullable = false)
    private Long systemUserId;

    @Column(name = "org_id", nullable = false)
    private String orgId;

    @Column(name = "role", nullable = false, columnDefinition = "SMALLINT")
    private Integer role;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "updated_by", nullable = false)
    private String updatedBy;

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