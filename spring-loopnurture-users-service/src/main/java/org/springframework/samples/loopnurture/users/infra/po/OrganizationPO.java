package org.springframework.samples.loopnurture.users.infra.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * 组织持久化对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "organization")
public class OrganizationPO {
    
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "org_code", unique = true)
    private String orgCode;

    @Column(name = "org_name")
    private String orgName;

    @Column(name = "org_type", columnDefinition = "SMALLINT")
    private Integer orgType;

    @Column(name = "org_status", columnDefinition = "SMALLINT")
    private Integer status;

    @Column(name = "description")
    private String description;

    @Column(name = "address")
    private String address;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @Column(name = "website")
    private String website;

    @Column(name = "logo_url")
    private String logoUrl;

    @Column(name = "max_users")
    private Integer maxUsers;

    @Column(name = "max_templates")
    private Integer maxTemplates;

    @Column(name = "max_rules")
    private Integer maxRules;

    @Column(name = "settings", columnDefinition = "jsonb")
    private String settings;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "updated_by", nullable = false)
    private String updatedBy;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (updatedAt == null) {
            updatedAt = LocalDateTime.now();
        }
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
} 