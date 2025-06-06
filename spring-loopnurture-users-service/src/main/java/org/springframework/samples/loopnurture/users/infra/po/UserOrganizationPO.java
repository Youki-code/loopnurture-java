package org.springframework.samples.loopnurture.users.infra.po;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 用户-组织关联持久化对象
 */
@Data
@Entity
@Table(name = "t_user_organization")
public class UserOrganizationPO {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "system_user_id", nullable = false)
    private Long systemUserId;

    @Column(name = "org_id", length = 64, nullable = false)
    private String orgId;

    @Column(name = "role", length = 20, nullable = false)
    private String role;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "created_by", length = 64, nullable = false)
    private String createdBy;

    @Column(name = "updated_by", length = 64, nullable = false)
    private String updatedBy;
} 