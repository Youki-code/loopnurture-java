package org.springframework.samples.loopnurture.users.domain.enums;

/**
 * 用户类型枚举
 * 区分用户的业务身份和权限范围
 */
public enum UserTypeEnum {
    /**
     * 个人用户
     * 使用免费版或基础版功能
     */
    PERSONAL,

    /**
     * 企业用户
     * 归属于某个企业组织，可以使用企业版功能
     */
    ENTERPRISE,

    /**
     * 系统管理员
     * 可以管理所有组织和用户
     */
    ADMIN,

    /**
     * 服务账号
     * 用于系统间集成，API调用等
     */
    SERVICE_ACCOUNT
} 