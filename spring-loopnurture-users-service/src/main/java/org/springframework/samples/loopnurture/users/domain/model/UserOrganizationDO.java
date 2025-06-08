package org.springframework.samples.loopnurture.users.domain.model;

import lombok.Data;
import org.springframework.samples.loopnurture.users.domain.enums.UserRoleEnum;
import java.time.LocalDateTime;

/**
 * 用户-组织关联领域对象
 * 表示用户在特定组织中的角色和权限
 */
@Data
public class UserOrganizationDO {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 系统用户ID
     */
    private Long systemUserId;

    /**
     * 组织ID
     */
    private String orgId;

    /**
     * 用户在组织中的角色
     */
    private UserRoleEnum role;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    /**
     * 创建人ID
     */
    private String createdBy;

    /**
     * 更新人ID
     */
    private String updatedBy;
} 