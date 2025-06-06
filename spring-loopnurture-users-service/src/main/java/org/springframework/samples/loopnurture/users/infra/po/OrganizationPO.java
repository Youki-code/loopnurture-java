package org.springframework.samples.loopnurture.users.infra.po;

import lombok.Data;
import org.springframework.samples.loopnurture.users.domain.enums.OrganizationStatusEnum;
import org.springframework.samples.loopnurture.users.domain.enums.OrganizationTypeEnum;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 组织持久化对象
 */
@Data
@Entity
@Table(name = "t_organization")
public class OrganizationPO {
    
    @Id
    @Column(name = "org_id", length = 64)
    private String orgId;

    @Column(name = "org_name", length = 100, nullable = false)
    private String orgName;

    @Column(name = "description", length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private OrganizationStatusEnum status;

    @Enumerated(EnumType.STRING)
    @Column(name = "org_type", length = 20, nullable = false)
    private OrganizationTypeEnum orgType;

    @Column(name = "settings", columnDefinition = "TEXT")
    private String settings;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "created_by", length = 64, nullable = false)
    private String createdBy;

    @Column(name = "updated_by", length = 64, nullable = false)
    private String updatedBy;
} 