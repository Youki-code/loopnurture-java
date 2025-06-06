-- 创建组织表
CREATE TABLE t_organization (
    org_id VARCHAR(64) PRIMARY KEY COMMENT '组织ID',
    org_name VARCHAR(100) NOT NULL COMMENT '组织名称',
    description VARCHAR(500) COMMENT '组织描述',
    status VARCHAR(20) NOT NULL COMMENT '组织状态：ACTIVE-正常，DISABLED-禁用，PENDING-待审核，DELETED-已删除',
    org_type VARCHAR(20) NOT NULL COMMENT '组织类型',
    settings TEXT COMMENT '组织设置，JSON格式',
    created_at DATETIME NOT NULL COMMENT '创建时间',
    updated_at DATETIME NOT NULL COMMENT '更新时间',
    created_by VARCHAR(64) NOT NULL COMMENT '创建人ID',
    updated_by VARCHAR(64) NOT NULL COMMENT '更新人ID',
    INDEX idx_created_at (created_at),
    INDEX idx_org_name (org_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='组织信息表'; 