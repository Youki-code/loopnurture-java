package org.springframework.samples.loopnurture.users.server.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 组织响应DTO
 */
@Data
public class OrganizationResponse {
    /**
     * 组织ID
     */
    private String id;

    /**
     * 组织名称
     */
    private String name;

    /**
     * 组织代码
     */
    private String code;

    /**
     * 组织描述
     */
    private String description;

    /**
     * 是否为个人组织
     */
    private boolean personal;

    /**
     * 组织状态
     */
    private String status;

    /**
     * 创建者ID
     */
    private String creatorId;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
} 