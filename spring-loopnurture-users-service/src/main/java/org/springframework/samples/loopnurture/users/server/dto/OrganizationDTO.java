package org.springframework.samples.loopnurture.users.server.dto;

import lombok.Data;
import org.springframework.samples.loopnurture.users.domain.enums.OrganizationStatusEnum;
import org.springframework.samples.loopnurture.users.domain.enums.OrganizationTypeEnum;
import java.time.LocalDateTime;

/**
 * 组织信息DTO
 */
@Data
public class OrganizationDTO {
    /**
     * 组织ID
     */
    private String orgId;

    /**
     * 组织名称
     */
    private String orgName;

    /**
     * 组织描述
     */
    private String description;

    /**
     * 组织状态
     */
    private OrganizationStatusEnum status;

    /**
     * 组织类型
     */
    private OrganizationTypeEnum orgType;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
} 