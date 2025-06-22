package org.springframework.samples.loopnurture.users.domain.model;

import lombok.Data;
import org.springframework.samples.loopnurture.users.domain.enums.OrganizationTypeEnum;
import org.springframework.samples.loopnurture.users.domain.enums.OrganizationStatusEnum;
import org.springframework.samples.loopnurture.users.domain.model.vo.OrganizationSettingsVO;

import java.time.LocalDateTime;

/**
 * 组织领域对象
 * 代表系统中的一个组织实体，包含组织的基本信息、配额限制、设置等
 */
@Data
public class OrganizationDO {
    /**
     * 组织代码
     */
    private String orgCode;

    /**
     * 组织名称
     */
    private String orgName;

    /**
     * 组织类型
     */
    private OrganizationTypeEnum orgType;

    /**
     * 组织状态
     */
    private OrganizationStatusEnum status;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 联系邮箱
     */
    private String email;

    /**
     * 组织网站
     */
    private String website;

    /**
     * 最大用户数
     */
    private Integer maxUsers;

    /**
     * 最大模板数
     */
    private Integer maxTemplates;

    /**
     * 最大规则数
     */
    private Integer maxRules;

    /**
     * 组织设置，包含配额限制、密码策略、通知设置等
     */
    private OrganizationSettingsVO extendsInfo;

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
    private Long createdBy;

    /**
     * 最后修改人ID，关联marketing_user.id
     */
    private Long updatedBy;
} 