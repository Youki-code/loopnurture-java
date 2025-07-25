package org.springframework.samples.loopnurture.users.server.controller.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 组织DTO
 */
@Data
public class OrganizationDTO {
    /**
     * 组织编码
     */
    private String orgCode;

    /**
     * 组织名称
     */
    private String orgName;

    /**
     * 组织状态
     */
    private Integer status;

    /**
     * 组织类型
     */
    private Integer orgType;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
} 