package org.springframework.samples.loopnurture.users.domain.model;

import lombok.Data;
import org.springframework.samples.loopnurture.users.domain.enums.*;
import org.springframework.samples.loopnurture.users.domain.vo.OrganizationSettingsVO;
import java.time.LocalDateTime;

/**
 * 组织领域对象
 * 代表系统中的一个组织实体，包含组织的基本信息、配额限制、设置等
 */
@Data
public class OrganizationDO {
    /**
     * 组织ID，系统生成的唯一标识符
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
     * 创建时间
     */
    private Long createTime;

    /**
     * 更新时间
     */
    private Long updateTime;

    /**
     * 组织类型
     */
    private OrganizationTypeEnum orgType;

    /**
     * 组织设置，包含配额限制、密码策略、通知设置等
     */
    private OrganizationSettingsVO settings = new OrganizationSettingsVO();

    /**
     * 记录创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 记录最后更新时间
     */
    private LocalDateTime updatedAt;

    /**
     * 创建人ID，关联marketing_user.id
     */
    private String createdBy;

    /**
     * 最后修改人ID，关联marketing_user.id
     */
    private String updatedBy;
} 