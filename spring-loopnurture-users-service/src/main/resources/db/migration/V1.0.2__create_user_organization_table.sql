-- 创建用户-组织关联表
CREATE TABLE t_user_organization (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    system_user_id BIGINT NOT NULL COMMENT '系统用户ID',
    org_id VARCHAR(64) NOT NULL COMMENT '组织ID',
    role VARCHAR(20) NOT NULL COMMENT '用户在组织中的角色',
    created_at DATETIME NOT NULL COMMENT '创建时间',
    updated_at DATETIME NOT NULL COMMENT '更新时间',
    created_by VARCHAR(64) NOT NULL COMMENT '创建人ID',
    updated_by VARCHAR(64) NOT NULL COMMENT '更新人ID',
    UNIQUE KEY uk_user_org (system_user_id, org_id),
    INDEX idx_org_id (org_id),
    INDEX idx_system_user_id (system_user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户-组织关联表'; 