package org.springframework.samples.loopnurture.users.infra.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

import java.time.LocalDateTime;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

/**
 * 组织持久化对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@org.hibernate.annotations.Where(clause = "deleted = false")
@Table(name = "organization")
public class OrganizationPO {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "org_code", unique = true)
    private String orgCode;

    @Column(name = "org_name")
    private String orgName;

    @Column(name = "org_type", columnDefinition = "SMALLINT")
    private Short orgType;

    @Column(name = "org_status", columnDefinition = "SMALLINT")
    private Short status;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @Column(name = "website")
    private String website;

    @Column(name = "max_users")
    private Integer maxUsers;

    @Column(name = "max_templates")
    private Integer maxTemplates;

    @Column(name = "max_rules")
    private Integer maxRules;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "extends_info", columnDefinition = "jsonb")
    private String extendsInfo;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "created_by", nullable = false)
    private Long createdBy;

    @Column(name = "updated_by", nullable = false)
    private Long updatedBy;

    @Column(name = "deleted", nullable = false)
    private Boolean deleted = false;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (updatedAt == null) {
            updatedAt = LocalDateTime.now();
        }
        if (deleted == null) deleted = false;
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
} 